package com.codit.talktalkcoach.external.gpt;

import com.codit.talktalkcoach.domain.enums.TargetLevel;
import com.codit.talktalkcoach.domain.enums.SpeechCategory;
import com.codit.talktalkcoach.exception.BusinessException;
import com.codit.talktalkcoach.exception.ErrorCode;
import com.codit.talktalkcoach.external.gpt.dto.GptAnalysisResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class GptClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${openai.api.key}")
    private String apiKey;

    private static final String GPT_URL = "https://api.openai.com/v1/chat/completions";
    private static final String MODEL   = "gpt-5.4-mini";

    // ─── 카테고리 지정 호출 ──────────────────────────────────────────
    public GptAnalysisResult analyzeSpeech(
            String transcript,
            TargetLevel targetLevel,
            SpeechCategory category
    ) {
        // 초1~2, 초3~4는 PRESENTATION만 허용
        SpeechCategory effectiveCategory = resolveCategory(targetLevel, category);

        String systemPrompt = buildSystemPrompt(targetLevel, effectiveCategory);
        String userPrompt   = buildUserPrompt(transcript);

        Map<String, Object> body = Map.of(
                "model", MODEL,
                "messages", List.of(
                        Map.of("role", "system", "content", systemPrompt),
                        Map.of("role", "user",   "content", userPrompt)
                ),
                "response_format", Map.of("type", "json_object"),
                "temperature", 0.4,
                "max_completion_tokens", 1800
        );

        try {
            ResponseEntity<Map> response = callGpt(body);
            String content = extractContent(response.getBody());
            log.debug("GPT 원본 응답: {}", content);

            GptAnalysisResult result = objectMapper.readValue(content, GptAnalysisResult.class);
            result.setWordCount(countWords(transcript));
            if (result.getDetectedCategory() == null) {
                result.setDetectedCategory(effectiveCategory.name());
            }
            return result;

        } catch (HttpClientErrorException e) {
            log.error("GPT API 클라이언트 오류 — status: {}, body: {}",
                    e.getStatusCode(), e.getResponseBodyAsString());
            throw new BusinessException(ErrorCode.GPT_API_ERROR);
        } catch (HttpServerErrorException e) {
            log.error("GPT API 서버 오류 — status: {}, body: {}",
                    e.getStatusCode(), e.getResponseBodyAsString());
            throw new BusinessException(ErrorCode.GPT_API_ERROR);
        } catch (Exception e) {
            log.error("GPT 분석 중 예외 — type: {}, message: {}",
                    e.getClass().getSimpleName(), e.getMessage(), e);
            throw new BusinessException(ErrorCode.GPT_API_ERROR);
        }
    }

    /** 하위 호환: 카테고리 미지정 시 PRESENTATION 기본 적용 */
    public GptAnalysisResult analyzeSpeech(String transcript, TargetLevel targetLevel) {
        return analyzeSpeech(transcript, targetLevel, SpeechCategory.PRESENTATION);
    }

    // ─── 카테고리 유효성 보정 ─────────────────────────────────────────
    /**
     * 교육과정 근거:
     * - 초1~2 ([2국01]): 발표형만 존재
     * - 초3~4 ([4국01]): 발표형 주, 의견/이유 제시 시작 (설득형·토론형 카테고리 미적용)
     * - 초5~6 이상: 연설·토론 담화 유형 정식 등장 → 3유형 모두 허용
     */
    private SpeechCategory resolveCategory(TargetLevel level, SpeechCategory requested) {
        if (level == TargetLevel.ELEM_1_2 || level == TargetLevel.ELEM_3_4) {
            if (requested != SpeechCategory.PRESENTATION) {
                log.warn("레벨 {}에서 {}는 지원하지 않습니다. PRESENTATION으로 대체합니다.",
                        level, requested);
            }
            return SpeechCategory.PRESENTATION;
        }
        return requested;
    }

    // ─── 공통 호출 ────────────────────────────────────────────────────
    private ResponseEntity<Map> callGpt(Map<String, Object> body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        log.debug("GPT 요청 모델: {}, API Key 앞 7자리: {}",
                MODEL, apiKey != null && apiKey.length() > 7 ? apiKey.substring(0, 7) : "없음");
        return restTemplate.exchange(
                GPT_URL, HttpMethod.POST, new HttpEntity<>(body, headers), Map.class);
    }

    @SuppressWarnings("unchecked")
    private String extractContent(Map<?, ?> responseBody) {
        return (String) ((Map<?, ?>) ((Map<?, ?>) ((List<?>) responseBody
                .get("choices")).get(0)).get("message")).get("content");
    }

    private int countWords(String transcript) {
        if (transcript == null || transcript.isBlank()) return 0;
        return transcript.trim().split("\\s+").length;
    }

    private String buildUserPrompt(String transcript) {
        return """
                ## 스피치 전사문
                %s
                
                위 스피치를 지정된 JSON 형식으로만 평가해주세요.
                """.formatted(transcript);
    }

    // ═══════════════════════════════════════════════════════════════
    // SYSTEM PROMPT
    // ═══════════════════════════════════════════════════════════════
    private String buildSystemPrompt(TargetLevel level, SpeechCategory category) {
        return String.format("""
                당신은 2022 개정 국어과 교육과정을 기반으로 학습자 스피치를 평가하는
                한국어 스피치 코칭 전문가입니다.
                
                ════════════════════════════════════════════════
                # 📜 코칭 철학 (절대 원칙)
                ════════════════════════════════════════════════
                1. **비판이 아닌 성장의 언어** — "틀렸습니다" ❌ → "이렇게 하면 더 좋아져요" ⭕
                2. 문제 지적 시 **주장 + 이유 + 대안**을 반드시 세트로 제시.
                   - "어휘가 단순합니다" ❌
                   - "'좋다'를 3번 반복했어요. 같은 단어를 반복하면 표현이 단조로워져요.
                      '인상 깊다', '만족스럽다'로 바꿔보세요." ⭕
                3. **학습자 수준 존중** — 저학년에게 추상어·반론 요구 ❌.
                4. 피드백에 **실제 표현을 따옴표로 인용** → 이유 설명 → 대안 제시.
                5. **오직 한국어**로만 응답.
                6. **칭찬은 결과/능력이 아닌 과정·성취 중심**으로.
                   - "머리가 좋네요" ❌ → "이 부분을 이렇게 표현해낸 점이 인상 깊어요" ⭕
                
                ════════════════════════════════════════════════
                # 🎯 평가 대상: %s
                ════════════════════════════════════════════════
                %s
                
                ════════════════════════════════════════════════
                # 🎤 스피치 유형: %s
                ════════════════════════════════════════════════
                %s
                
                ════════════════════════════════════════════════
                # ⚠️ 상대 평가 원칙 (CRITICAL)
                ════════════════════════════════════════════════
                점수는 **"해당 학년군 평균 학습자 대비 상대적 품질"**입니다.
                동일한 스피치라도 평가 대상 레벨이 낮을수록 점수가 높아야 정상입니다.
                
                **고등학생 수준의 스피치가 입력되면:**
                | 레벨 | 예상 점수 | 이유 |
                | ELEM_1_2  | 94~99 | 기대치 압도적 초과 |
                | ELEM_3_4  | 91~96 | 기대치 크게 초과 |
                | ELEM_5_6  | 86~92 | 기대치 상당히 초과 |
                | MIDDLE_1_2| 79~86 | 기대치 부합, 약간 우수 |
                | MIDDLE_3  | 73~81 | 기대치 부합, 평균적 |
                
                **초1~2 수준의 단순한 스피치가 입력되면:**
                | 레벨 | 예상 점수 |
                | ELEM_1_2  | 76~84 |
                | ELEM_3_4  | 65~74 |
                | ELEM_5_6  | 54~64 |
                | MIDDLE_1_2| 44~54 |
                | MIDDLE_3  | 38~48 |
                
                👉 현재 평가 대상: **[%s]** — 이 레벨 평균과 비교하여 채점하세요.
                
                ⚠️ 채점 전 반드시 스스로 확인하세요:
                “이 스피치를 ELEM_1_2로 평가한다면 몇 점이 나오는가? MIDDLE_3로 평가한다면 몇 점이 나오는가?”
                — 두 점수의 차이가 반드시 15점 이상 나와야 합니다.
                — 차이가 5점 이하라면 상대 평가가 제대로 적용되지 않은 것입니다. 다시 검토하세요.
                
                ════════════════════════════════════════════════
                # 🎚 [%s] 전용 점수 밴드
                ════════════════════════════════════════════════
                %s
                
                ════════════════════════════════════════════════
                # 📊 4개 평가 항목 (각 0-100, 서브 루브릭 합산)
                ════════════════════════════════════════════════
                각 항목을 4개 서브컴포넌트(각 0-25점)로 **내부 채점 후 합산**하세요.
                이 과정이 자연스럽게 비-5배수 점수를 만들어냅니다.
                
                ## 1. vocabularyScore (단어)
                - 학년 적합성        (0-25): 해당 학년군에 맞는 어휘인가
                - 어휘 다양성        (0-25): 같은 단어 과도한 반복 없음
                - 단어 정확성        (0-25): 문맥에 맞는 정확한 낱말 선택
                - 표현 풍부함        (0-25): 레벨별 가중치 적용
                  → 초1~4: 구체적 단어, 일상어 중심
                  → 초5~중2: 접속어, 개념어 활용
                  → 중3: 추상어, 논리어 적절 활용
                
                ## 2. sentenceScore (문법)
                - 문장 완결성        (0-25): 주어-서술어 호응
                - 조사·어미 정확성   (0-25)
                - 복문 구사력        (0-25): 학년군에 맞는 수준으로
                - 문장 리듬감        (0-25): 짧은 문장 나열 ❌, 지나치게 긴 문장 ❌
                
                ⚠️ 구어체 특성상 문어체 기준을 엄격하게 적용하지 마세요.
                최소 규범(주어-서술어 호응, 심각한 조사 오류)만 지적하고,
                "~거든요", "~잖아요" 등의 구어 종결어미는 상황에 맞으면 감점 금지.
                
                ## 3. logicScore (논리) — 유형별 기준 상이
                %s
                
                ## 4. structureScore (구조) — 유형별 기준 상이
                %s
                
                ## 5. prosodyScore (운율·리듬감 간접 추정)
                ※ Azure ko-KR은 prosody를 미지원하므로 텍스트 구조로 간접 추정합니다.
                   아래 4개 지표 각 0-25점으로 내부 채점 후 합산하세요.
                
                ① 문장 길이 다양성 (0-25)
                   - 짧은 문장과 긴 문장이 적절히 섞여 있으면 높은 점수.
                   - "~합니다. ~입니다. ~합니다." 동일 패턴 반복 → 낮은 점수.
                   - 강조용 짧은 단문 + 설명용 긴 복문 조합 → 높은 점수.
                
                ② 호흡 단위 표지 (0-25)
                   - 쉼표(,), 접속어("그러나", "그래서", "왜냐하면"), 부사어가
                     자연스러운 호흡 위치에 배치되면 높은 점수.
                   - 접속어 없이 문장만 나열 → 낮은 점수.
                   - 쉼표 없이 100자 이상 이어지는 문장 → 낮은 점수.
                
                ③ 강조·변화 표현 (0-25)
                   - 반복("정말", "반드시"), 열거("첫째~둘째~셋째"),
                     대조("하지만", "반면에") 같은 강세 변화를 유발하는 장치 존재 시 높은 점수.
                   - 아무런 강조 장치 없이 서술만 → 낮은 점수.
                
                ④ 청중 상호작용 표현 (0-25)
                   - "여러분", "~해보신 적 있나요?", "한번 생각해볼까요?" 등
                     청중을 향한 직접 표현 → 높은 점수.
                   - ELEM_1_2~3_4: 없어도 감점 없음.
                   - ELEM_5_6 이상: 1개 이상이면 가점.
                
                ⚠️ prosodyScore는 간접 추정임을 인지하고,
                   텍스트 근거 없이 임의로 높거나 낮은 점수를 주지 마세요.
                   반드시 4개 지표 합산으로 도출하세요. (5배수 금지)
                
                prosodyScore 기준 예시:
                - 문장 다양 + 접속어 풍부 + 강조 표현 + 청중 호명 → 82~91
                - 문장 보통 + 접속어 일부 + 강조 표현 없음      → 64~74
                - 동일 패턴 반복 + 접속어 없음 + 청중 호명 없음  → 45~62
                - "~합니다" 단조 나열만                          → 38~52
                
                ════════════════════════════════════════════════
                # 🔢 점수 세밀도 규칙
                ════════════════════════════════════════════════
                - 최종 점수: 1~100 정수.
                - **5의 배수(70, 75, 80, 85, 90, 95)를 의도적으로 피하세요.**
                - 서브 루브릭 합산이 5의 배수가 되면 ±1~2점 재검토하여 조정.
                - ✅ 좋은 예: 82, 87, 73, 91, 68, 79, 84, 93
                - ❌ 나쁜 예: 80, 85, 75, 90, 70, 95
                
                ════════════════════════════════════════════════
                # 💬 피드백 작성 규칙
                ════════════════════════════════════════════════
                %s
                
                ## ❌ 나쁜 피드백 (이유 없음)
                "'관점'으로 바꾸세요."
                → 왜 바꿔야 하는지 모름.
                
                ## ⭕ 좋은 피드백 (주장 + 이유 + 대안)
                "'저자의 시선을 통해'라는 표현이 사용되었는데, '시선'보다 '관점'이 
                 더 격식 있는 표현이에요. '저자의 관점을 통해'로 바꾸면 더 자연스럽습니다."
                
                ════════════════════════════════════════════════
                # 📌 customPlan 규칙
                ════════════════════════════════════════════════
                %s
                
                ════════════════════════════════════════════════
                # 📤 응답 JSON 형식 (이 형식으로만. 그 외 텍스트 금지)
                ════════════════════════════════════════════════
                {
                  "scoringRationale": "<[%s] 평균과 비교한 채점 근거 1~2문장>",
                  "detectedCategory": "%s",
                  "title": "<스피치 주제를 5단어 이내로 요약. 예: \"독서의 중요성\", \"환경 보호 필요성\", \"우정이 가지는 의미\">",
                  "vocabularyScore": <1~100 정수, 5배수 금지>,
                  "logicScore": <1~100 정수, 5배수 금지>,
                  "structureScore": <1~100 정수, 5배수 금지>,
                  "prosodyScore": <1~100 정수, 5배수 금지, 위 4개 지표 합산>,
                  "wordCount": <공백 기준 정수>,
                  "vocabularyFeedback": %s,
                  "sentenceStructureFeedback": %s,
                  "logicFeedback": %s,
                  "overallFeedback": %s,
                  "customPlan": [
                    {"title": "...", "description": "...", "category": "..."},
                    {"title": "...", "description": "...", "category": "..."},
                    {"title": "...", "description": "...", "category": "..."}
                  ]
                }
                
                ════════════════════════════════════════════════
                # 🎓 Few-shot 예시
                ════════════════════════════════════════════════
                %s
                """,
                getLevelDescription(level),         // 평가 대상 헤더
                getLevelExpectations(level, category),
                getCategoryLabel(category),
                getCategoryGuide(category),
                getLevelDescription(level),         // 상대평가 매트릭스 안의 레벨
                getLevelDescription(level),         // 점수 밴드 헤더
                getScoreBands(level),
                getLogicRubric(category),
                getStructureRubric(category),
                getFeedbackStyleGuide(level),
                getCustomPlanGuide(level),
                getLevelDescription(level),         // scoringRationale 템플릿
                category.name(),
                getFeedbackFormat(level, "vocabularyFeedback"),
                getFeedbackFormat(level, "sentenceStructureFeedback"),
                getFeedbackFormat(level, "logicFeedback"),
                getFeedbackFormat(level, "overallFeedback"),
                getFewShotExamples(level, category)
        );
    }

    // ─── 레벨 설명 ────────────────────────────────────────────────────
    private String getLevelDescription(TargetLevel level) {
        return switch (level) {
            case ELEM_1_2   -> "초등학교 1~2학년 [2국01 기반]";
            case ELEM_3_4   -> "초등학교 3~4학년 [4국01 기반]";
            case ELEM_5_6   -> "초등학교 5~6학년 [6국01 기반]";
            case MIDDLE_1_2 -> "중학교 1~2학년 [9국01 기반]";
            case MIDDLE_3   -> "중학교 3학년 [9국01 심화]";
        };
    }

    // ─── 카테고리 설명 ────────────────────────────────────────────────
    private String getCategoryLabel(SpeechCategory c) {
        return switch (c) {
            case PRESENTATION -> "발표형 (정보 전달)";
            case SPEECH       -> "설득·연설형";
            case DEBATE       -> "토론·논증형";
        };
    }

    private String getCategoryGuide(SpeechCategory c) {
        return switch (c) {
            case PRESENTATION -> """
                    정보를 체계적으로 전달하는 발표입니다. 2022 교육과정의 핵심 말하기 유형.
                    - 핵심: 청중이 이해하기 쉽도록 내용을 조직하는 능력.
                    - ⚠️ "주장-이유-근거-반론" 구조를 요구하지 마세요. 이는 토론형 기준입니다.
                    - 평가 초점: 도입-전개-정리의 조직성, 설명 방법의 다양성, 청중 고려.
                    """;
            case SPEECH -> """
                    청중을 설득하거나 감화하는 연설입니다.
                    - 핵심: 주장-이유-예시의 흐름, 청중 공감 유발.
                    - 평가 초점: 주장의 명확성, 이유·예시의 설득력, 감정 호소.
                    """;
            case DEBATE -> """
                    논제에 대해 논증하는 토론·논설입니다.
                    - 핵심: 주장-근거(수치·사례)-반론 인정.
                    - 평가 초점: 근거의 구체성, 논리적 일관성, 반론 대응.
                    """;
        };
    }

    // ─── 레벨 × 카테고리 기대 수준 (2022 교육과정 성취기준 직접 반영) ─
    private String getLevelExpectations(TargetLevel level, SpeechCategory category) {
        return switch (level) {
            case ELEM_1_2 -> """
                    [2022 교육과정 근거: 2국01]
                    [어휘] 일상 단어 중심. 또래가 아는 쉬운 단어면 OK. 반복만 없으면 충분.
                    [문법] 주어+서술어가 갖춰진 단문이면 OK. 바르고 고운 말.
                    [논리] 일이 일어난 순서가 전달되면 OK. 이유 1개만 있어도 훌륭.
                    [구조] 시작-중간-끝 구분만 있어도 OK.
                    ⚠️ 이 나이에 복잡한 조직이나 논거를 요구하지 마세요.
                    """;
            case ELEM_3_4 -> """
                    [2022 교육과정 근거: 4국01]
                    [어휘] 또래 교과서 수준 어휘. 다소 반복이 있어도 OK. 틀린 단어 사용만 지적.
                    [문법] 주어-서술어 호응. 원인-결과를 나타내는 문장 사용 시도.
                    [논리] 원인-결과 구조로 설명. 주제에 적절한 의견+이유 1개 제시면 OK.
                    [구조] 처음-가운데-끝. 원인-결과 순서대로 조직되면 OK.
                    ⚠️ '의견과 이유 제시'가 시작 단계이므로, 근거·반론 요구 ❌.
                    """;
            case ELEM_5_6 -> switch (category) {
                case PRESENTATION -> """
                        [2022 교육과정 근거: 6국01]
                        [어휘] 다양한 어휘 사용. 간단한 개념어, 접속어('그래서', '따라서') 활용.
                        [문법] 문장 성분 호응, 시제 일관성. 복문('~기 때문에 ~합니다') 시도.
                        [논리] 핵심 정보 중심으로 내용 구성. 설명 방법(예시, 비교) 1가지 활용.
                        [구조] 도입-전개-정리의 기본 골격. 매체 자료 활용하면 우수.
                        """;
                case SPEECH -> """
                        [2022 교육과정 근거: 6국01 연설]
                        [어휘] 감정 표현어, 설득 표현어 활용.
                        [문법] 호응 맞춰 주장 말하기. 간단한 복문.
                        [논리] **주장+이유+근거** 구성. (교육과정 6학년 핵심 성취기준)
                        [구조] 서-본-결의 기본 골격. 감정 흐름 일관성.
                        """;
                case DEBATE -> """
                        [2022 교육과정 근거: 6국01 토론]
                        [어휘] 논리적 연결어, 정확한 단어.
                        [문법] 조건·인과 복문.
                        [논리] 주장+이유+근거. 논거의 타당성. (반론 인정은 아직 ❌)
                        [구조] 쟁점 제시, 논거 순서, 기본 전환어 활용.
                        """;
            };
            case MIDDLE_1_2 -> switch (category) {
                case PRESENTATION -> """
                        [2022 교육과정 근거: 9국01]
                        [어휘] 교과서 수준 + 개념어, 사고도구어 활용. 정확한 단어 선택.
                        [문법] 주어-서술어 호응 필수. 복문 자연스럽게.
                        [논리] 체계적 내용 구성. 핵심 정보 중심 조직. 설명 방법 다양하게.
                        [구조] 도입-전개-정리 탄탄. 청자가 이해하기 쉽게 재구성.
                        """;
                case SPEECH -> """
                        [2022 교육과정 근거: 9국01 연설]
                        [어휘] 수사적 표현, 호소력 있는 어휘.
                        [문법] 복문과 단문의 조화, 수사 기법.
                        [논리] 주장+이유+예시. 청중 공감 유발. **반론 고려 시작**.
                        [구조] 서-본-결, 감정 흐름의 일관성.
                        """;
                case DEBATE -> """
                        [2022 교육과정 근거: 9국01 토론]
                        [어휘] 논리어, 정확한 용어.
                        [문법] 조건·인과 복문, 정확한 호응.
                        [논리] 주장+이유+근거. **반론 고려**하여 논증 구성. (교육과정 핵심)
                        [구조] 논리적 단락, 전환어, 쟁점 제시.
                        """;
            };
            case MIDDLE_3 -> switch (category) {
                case PRESENTATION -> """
                        [2022 교육과정 근거: 9국01 심화 + 고입 준비 수준]
                        [어휘] 추상어, 논리어, 사회문화적 맥락에 맞는 어휘.
                        [문법] 호응 관계, 복문·단문 리듬감 있게 혼용.
                        [논리] 청중(담화공동체) 고려하여 요점 중심 조직. 사회문화적 맥락 반영.
                        [구조] 도입-전개-정리 완성도 높음. 단락 간 유기적 연결.
                        """;
                case SPEECH -> """
                        [2022 교육과정 근거: 9국01 심화]
                        [어휘] 추상어, 수사적 어휘, 비유·은유.
                        [문법] 부사-서술어 호응('결코 ~ 않다'), 리듬감.
                        [논리] 주장+이유+예시+공감 장치. 설득 전략 평가 능력 반영.
                        [구조] 감정 곡선, 수사적 절정 배치.
                        """;
                case DEBATE -> """
                        [2022 교육과정 근거: 9국01 심화]
                        [어휘] 논리어('반면에', '결론적으로'), 전문 용어.
                        [문법] 복문 구사, 정확한 호응.
                        [논리] 주장+이유+**근거(수치·사례)** + **반론 인정** + 재반론. 논증 타당성 엄격 평가.
                        [구조] 논증 단락 유기적 연결, 전환어 풍부.
                        """;
            };
        };
    }

    // ─── 논리·구조 서브 루브릭 (유형별) ────────────────────────────────
    private String getLogicRubric(SpeechCategory c) {
        return switch (c) {
            case PRESENTATION -> """
                    - 정보의 정확성       (0-25)
                    - 설명 순서의 체계성  (0-25): 원인-결과, 순서 등
                    - 청중 맞춤 수준      (0-25): 용어·예시가 청중에게 적절한가
                    - 설명 방법 활용      (0-25): 예시/비교/분류/인과 등
                    """;
            case SPEECH -> """
                    - 주장의 명확성       (0-25)
                    - 이유·예시의 설득력  (0-25)
                    - 공감 유발력         (0-25): 경험·감정 언급 등
                    - 문장 간 흐름        (0-25): 논리적 연결
                    """;
            case DEBATE -> """
                    - 주장의 명확성       (0-25)
                    - 근거의 구체성       (0-25): 수치·사례 포함 여부
                    - 논리적 일관성       (0-25): 비약 없음
                    - 반론 인식·대응      (0-25): MIDDLE_1_2 이상에서 본격 평가
                    """;
        };
    }

    private String getStructureRubric(SpeechCategory c) {
        return switch (c) {
            case PRESENTATION -> """
                    - 도입부 효과성     (0-25): 주제 예고, 흥미 유발
                    - 전개부 조직성     (0-25): 논리 순서, 내용 재구성
                    - 정리부 완결성     (0-25): 요약·마무리
                    - 청중 상호작용 고려(0-25): MIDDLE_1_2 이상에서 가중
                    """;
            case SPEECH -> """
                    - 도입(문제 제기·훅)(0-25)
                    - 본론 전개          (0-25)
                    - 결론(행동 촉구)    (0-25)
                    - 감정 곡선 통일성   (0-25)
                    """;
            case DEBATE -> """
                    - 쟁점 제시          (0-25)
                    - 논거 배치 순서     (0-25)
                    - 반론·재반론 단락   (0-25): MIDDLE_1_2 이상에서 본격 평가
                    - 전환어 활용        (0-25)
                    """;
        };
    }

    // ─── 점수 밴드 (교육과정 기반 레벨별) ─────────────────────────────
    private String getScoreBands(TargetLevel level) {
        return switch (level) {
            case ELEM_1_2 -> """
                    - **91~100**: 또래보다 훨씬 뛰어난 표현. 이유·순서가 명확.
                    - **81~90**: 완전한 문장, 시작-중간-끝 갖춤. 또래 평균 이상.
                    - **71~80**: 주어-서술어는 있으나 짧은 문장 나열, 순서 흐림.
                    - **61~70**: 문장 미완성 혹은 주제 파악 어려움.
                    - **60 이하**: 의미 전달 자체가 어려움.
                    ⚠️ 이 나이에 복잡한 구조를 기대하지 마세요. 전달이 되면 후한 점수.
                    """;
            case ELEM_3_4 -> """
                    - **91~100**: 원인-결과 구조 자연스럽고 의견+이유 2개 이상 명확.
                    - **81~90**: 원인-결과 조직, 의견+이유 1개 명확. 평균 이상.
                    - **71~80**: 기본 구조 있으나 이유가 추상적 혹은 연결 어색.
                    - **61~70**: 문장 호응 오류 혹은 주제 이탈.
                    - **60 이하**: 흐름 단절.
                    """;
            case ELEM_5_6 -> """
                    - **91~100**: 주장+이유+근거 구성, 설명 방법 다양, 전달 우수.
                    - **83~90**: 핵심 정보 중심 조직, 이유·근거 제시. 평균 이상.
                    - **73~82**: 구조는 있으나 설명 방법 단조 혹은 근거 부족. 평균.
                    - **63~72**: 호응 오류 혹은 조직 미흡.
                    - **62 이하**: 수준 미달.
                    """;
            case MIDDLE_1_2 -> """
                    - **91~100**: 체계적 조직, 설명 방법 다양, 반론 고려, 사회문화적 맥락 반영.
                    - **82~90**: 구조 명확, 복문 자연스럽고 근거 있음. 평균 이상.
                    - **72~81**: 구조는 있으나 근거 부족 혹은 반론 미고려. 중등 평균.
                    - **62~71**: 호응 실수, 조직 단조.
                    - **61 이하**: 중등 수준 미달.
                    """;
            case MIDDLE_3 -> """
                    - **91~100**: 논증 정교, 수치·사례 근거, 반론+재반론, 담화공동체 고려.
                    - **83~90**: 주장+이유+근거 탄탄, 반론 인정 또는 사회문화적 맥락 중 1개.
                    - **73~82**: 구조 있으나 구체 근거 부족 혹은 반론 미고려. 중3 평균.
                    - **63~72**: 주장 반복, 논거 빈약, 전환어 부족.
                    - **62 이하**: 중1~2 수준 이하.
                    ⚠️ MIDDLE_3은 기준이 가장 엄격합니다. 근거 부재 시 80점 초반이 적절.
                    """;
        };
    }

    // ─── 레벨별 피드백 스타일 가이드 ────────────────────────────────────
    private String getFeedbackStyleGuide(TargetLevel level) {
        return switch (level) {
            case ELEM_1_2 -> """
                    - **문장: 짧고 단순하게** — 1~2문장 이내. 복잡한 설명 금지.
                    - **칭찬 비중: 높게** — 잘한 점 2개 + 개선 1개 비율.
                    - **표현**: "~가 참 좋았어요!" / "~하면 더 잘 할 수 있어요."
                    - 설명 없이 '고치라'만 하지 말고, **왜 고치면 좋은지** 1문장 추가.
                    """;
            case ELEM_3_4 -> """
                    - **문장: 2~3문장**, 쉬운 어휘.
                    - **칭찬 비중: 높게** — 잘한 점 2개 + 개선 1개 비율. (저학년과 동일하게 칭찬 중심)
                    - **표현**: "~하는 게 정말 잘 됐어요!", "~이 인상 깊었어요!" / "~를 바꿔보면 더 좋을 것 같아요."
                    - 개선 지적 시 **이유 한 문장 필수** + 구체적 대안 제시.
                    """;
            case ELEM_5_6, MIDDLE_1_2, MIDDLE_3 -> """
                    - **문장: 2~3문장**.
                    - **칭찬 비중: 1:1** (잘한 점 1 + 개선 1)
                    - **표현**: ELEM_5_6 → "~해보면 어떨까요?" 가능.
                             MIDDLE → "~면 더 완성도 있는 스피치가 될 것입니다."
                    - 개선 지적 시 반드시 **주장(문제) + 이유(왜 문제인가) + 대안(어떻게 고치나)** 세트.
                    """;
        };
    }

    // ─── 레벨별 customPlan 개수 및 스타일 ────────────────────────────────
    private String getCustomPlanGuide(TargetLevel level) {
        return switch (level) {
            case ELEM_1_2 -> """
                    **1~2개** 실천 항목 (저학년은 적게, 읽기 부담 고려).
                    - title: 5~8자 이내 (매우 짧게)
                    - description: 1문장만. 쉬운 말로.
                    - category: VOCABULARY | SENTENCE | LOGIC | STRUCTURE
                    예: {"title": "이유 말하기", "description": "'왜냐하면'으로 시작하는 이유 문장을 하나 더 말해보세요.", "category": "LOGIC"}
                    """;
            case ELEM_3_4 -> """
                    **2~3개** 실천 항목.
                    - title: 8자 내외
                    - description: 1~2문장. 구체적인 연습 방법 포함.
                    - category: VOCABULARY | SENTENCE | LOGIC | STRUCTURE
                    """;
            case ELEM_5_6, MIDDLE_1_2, MIDDLE_3 -> """
                    **3~4개** 실천 항목.
                    - title: 10자 내외
                    - description: 1~2문장. "무엇을, 어떻게, 얼마나" 구체 명시.
                    - category: VOCABULARY | SENTENCE | LOGIC | STRUCTURE | PACING | FILLER
                    예: {"title": "주장 뒤 근거 붙이기", "description": "주장 후 '왜냐하면' 또는 '실제로'로 시작하는 근거 문장을 하루 3문장씩 작성해보세요.", "category": "LOGIC"}
                    """;
        };
    }

    // ─── JSON 필드별 피드백 길이 지정 ─────────────────────────────────
    private String getFeedbackFormat(TargetLevel level, String field) {
        boolean isShort  = (level == TargetLevel.ELEM_1_2);
        boolean isMedium = (level == TargetLevel.ELEM_3_4);

        if (field.equals("overallFeedback")) {
            if (isShort)  return "\"<2~3문장. 칭찬 2 + 개선 1 + 응원. 쉬운 말로.>\"";
            if (isMedium) return "\"<3문장. 칭찬 1 + 개선 1 + 응원.>\"";
            return "\"<3~4문장. 칭찬 1 + 개선 1 + 응원. 과정/성취 중심 칭찬.>\"";
        }
        if (isShort)  return "\"<1~2문장. 쉬운 말로.>\"";
        if (isMedium) return "\"<2문장.>\"";
        return "\"<2~3문장.>\"";
    }

    // ─── Few-shot 예시 (레벨 × 카테고리) ────────────────────────────────
    private String getFewShotExamples(TargetLevel level, SpeechCategory category) {
        if (level == TargetLevel.ELEM_1_2) {
            return """
                    [예시 입력]
                    "저는 강아지를 좋아해요. 강아지는 귀여워요. 우리 강아지 이름은 콩이에요."
                    
                    [ELEM_1_2 기대 채점]
                    vocabularyScore: 74, sentenceScore: 78, logicScore: 69, structureScore: 72
                    
                    [좋은 vocabularyFeedback - 짧고 칭찬 중심]
                    "'귀여워요'라는 표현이 콩이의 느낌을 잘 전달했어요! '귀엽고 사랑스러워요'처럼 
                     표현을 조금 더 늘려보면 더 풍부해질 것 같아요."
                    
                    [좋은 overallFeedback]
                    "콩이에 대한 사랑이 느껴졌어요! 강아지를 좋아하는 마음이 잘 전달됐습니다. 
                     다음에는 '왜냐하면 콩이가 나를 반겨줘서요'처럼 이유를 하나 더 말해보세요. 
                     정말 잘했어요!"
                    
                    [좋은 customPlan]
                    {"title": "이유 말하기", "description": "'왜냐하면'으로 시작하는 이유 한 문장을 더 말해보세요.", "category": "LOGIC"}
                    """;
        }
        if (level == TargetLevel.ELEM_3_4) {
            return """
                    [예시 입력]
                    "환경이 중요합니다. 쓰레기를 버리면 안 됩니다. 우리 모두 잘 해야 합니다."
                    
                    [ELEM_3_4 기대 채점]
                    vocabularyScore: 72, sentenceScore: 74, logicScore: 67, structureScore: 69
                    
                    [좋은 logicFeedback - 이유 포함]
                    "'쓰레기를 버리면 안 된다'는 주장이 명확해요. 
                     이유가 있으면 더 설득력 있어요. '쓰레기를 버리면 땅이 오염되기 때문입니다'처럼 
                     원인을 한 문장 추가해보세요."
                    
                    [좋은 customPlan]
                    {"title": "원인 말하기", "description": "주장 뒤에 '왜냐하면'이나 '~때문에'로 이유를 한 문장 붙이는 연습을 매일 해보세요.", "category": "LOGIC"}
                    """;
        }
        if (level == TargetLevel.ELEM_5_6 && category == SpeechCategory.PRESENTATION) {
            return """
                    [예시 입력]
                    "오늘은 분리수거에 대해 발표하겠습니다. 분리수거는 중요합니다.
                     플라스틱과 종이를 따로 버려야 합니다. 그렇게 하면 환경을 지킬 수 있습니다."
                    
                    [ELEM_5_6 × PRESENTATION 기대 채점]
                    vocabularyScore: 72, sentenceScore: 74, logicScore: 68, structureScore: 63
                    
                    [좋은 structureFeedback - 이유 포함]
                    "발표의 시작과 끝이 명확해서 좋았어요!
                     '예를 들어, 플라스틱 병 하나가 분해되는 데 500년이 걸려요'처럼 
                     구체적인 예시를 하나 추가하면 발표가 훨씬 풍부해집니다.
                     마무리에 '오늘 발표한 내용을 정리하면'으로 시작하는 정리 문장도 넣어보세요."
                    
                    [좋은 customPlan]
                    {"title": "구체적 예시 추가", "description": "주장 뒤에 '예를 들어' 또는 '실제로'로 시작하는 구체적 사례 1개를 발표에 넣는 연습을 해보세요.", "category": "LOGIC"}
                    """;
        }
        if ((level == TargetLevel.MIDDLE_1_2 || level == TargetLevel.MIDDLE_3)
                && category == SpeechCategory.DEBATE) {
            return """
                    [예시 입력]
                    "환경 보호가 중요합니다. 기후변화가 심각합니다. 모두 실천해야 합니다.
                     결코 환경 보호는 포기하면 안 됩니다. 따라서 노력이 필요합니다."
                    
                    [MIDDLE × DEBATE 기대 채점]
                    vocabularyScore: 71, sentenceScore: 68, logicScore: 61, structureScore: 67
                    
                    [좋은 logicFeedback - 주장+이유+대안]
                    "'결코 환경 보호는 포기하면 안 됩니다'에서 '결코'는 반드시 부정문과 호응해야 해요.
                     이 문장처럼 서술어가 '안 됩니다'이면 맞지만, 
                     '결코 포기해서는 안 됩니다'로 고치면 더 자연스럽습니다.
                     또한 주장과 이유는 있으나 구체적 근거가 부족해요.
                     '최근 10년간 평균 기온이 1.5도 상승했다'처럼 수치를 추가하면 설득력이 올라갑니다."
                    
                    [좋은 customPlan]
                    {"title": "반론 인정 구조", "description": "'물론 경제적 비용이 들지만'처럼 상대 의견을 먼저 인정한 뒤 자신의 주장을 제시하는 구조를 한 문단에 1회 넣어보세요.", "category": "LOGIC"}
                    """;
        }
        if (category == SpeechCategory.SPEECH) {
            return """
                    [예시 입력 — SPEECH]
                    "여러분, 독서는 좋습니다. 책을 읽으면 좋습니다. 책을 읽으세요. 감사합니다."
                    
                    [기대 채점]
                    vocabularyScore: 67, sentenceScore: 71, logicScore: 62, structureScore: 63
                    
                    [좋은 logicFeedback - 주장+이유+대안]
                    "'책을 읽으면 좋습니다'라는 주장이 반복되고 있어요.
                     같은 말을 반복하면 청중의 공감을 얻기 어려워집니다.
                     '지난달 책을 한 권 읽었더니 발표하기가 더 쉬워졌어요'처럼 
                     본인 경험을 한 문장 추가하면 훨씬 설득력이 생깁니다."
                    
                    [좋은 customPlan]
                    {"title": "개인 경험 인용", "description": "주장 뒤에 '저는 ~한 적이 있어요'로 시작하는 개인 경험을 한 문장 추가해보세요.", "category": "LOGIC"}
                    """;
        }
        // 기본값
        return """
                [예시 입력]
                "환경 문제는 우리가 함께 노력해야 합니다. 환경이 중요합니다.
                 분리수거를 해야 합니다. 그러면 좋아질 것 같습니다."
                
                [기대 채점]
                vocabularyScore: 71, sentenceScore: 67, logicScore: 63, structureScore: 68
                
                [좋은 sentenceStructureFeedback]
                "'환경 문제는 노력해야 합니다'는 주어와 서술어가 어색해요.
                 주어와 서술어가 호응하지 않으면 뜻이 모호해질 수 있어요.
                 '환경 문제를 해결하려면 우리가 노력해야 합니다'로 바꿔보세요."
                """;
    }

    // ─── 요약 피드백 (JSON 배열로 반환) ────────────────────────────────────
    public List<String> generateSummaryFeedback(String analysisContext, TargetLevel targetLevel) {
        String levelDesc = getLevelDescription(targetLevel);
        String prompt = String.format("""
                당신은 친근하고 격려를 잘 하는 한국어 스피치 코치입니다.
                아래는 [%s] 수준 학습자의 최근 스피치 분석 결과 요약입니다.
                
                %s
                
                위 데이터를 바탕으로 아래 조건에 맞는 종합 피드백 3가지를 JSON 배열로 작성하세요.
                
                조건:
                1. 피드백은 반드시 3개
                2. 각 피드백은 1~2문장 이내
                3. 피드백 구성: 잘한 점 1개 + 개선할 점 1개 + 응원 1개
                4. 학습자 수준([%s])에 맞는 어휘 사용
                5. 한국어로만
                
                출력 형식 (JSON만, 다른 텍스트 없음):
                ["피드백1", "피드백2", "피드백3"]
                """, levelDesc, analysisContext, levelDesc);

        // response_format 제거 — JSON 배열를 직접 반환받기 위해 json_object 사용 불가
        Map<String, Object> body = Map.of(
                "model", MODEL,
                "messages", List.of(Map.of("role", "user", "content", prompt)),
                "temperature", 0.7,
                "max_completion_tokens", 500
        );
        try {
            ResponseEntity<Map> response = callGpt(body);
            String content = extractContent(response.getBody()).trim();
            log.debug("GPT 요약 피드백 원문: {}", content);

            // JSON 배열 위치 찾기
            String jsonStr = content;
            if (!content.startsWith("[")) {
                int start = content.indexOf("[");
                int end   = content.lastIndexOf("]");
                if (start >= 0 && end > start) {
                    jsonStr = content.substring(start, end + 1);
                } else {
                    log.warn("JSON 배열 미발견, 줄바꾸메 분리: {}", content);
                    return java.util.Arrays.stream(content.split("\\n"))
                            .map(String::trim)
                            .filter(s -> !s.isBlank())
                            .limit(3)
                            .collect(java.util.stream.Collectors.toList());
                }
            }

            String[] arr = new com.fasterxml.jackson.databind.ObjectMapper()
                    .readValue(jsonStr, String[].class);
            List<String> result = new java.util.ArrayList<>(java.util.Arrays.asList(arr));
            if (result.size() != 3) {
                log.warn("GPT 피드백 개수 이상: {}(3개 기대)", result.size());
            }
            log.info("AI 피드백 배열 생성 완료: {}개", result.size());
            return result;
        } catch (Exception e) {
            log.error("GPT 요약 피드백 오류: {}", e.getMessage());
            throw new BusinessException(ErrorCode.GPT_API_ERROR);
        }
    }
}