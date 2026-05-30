package com.codit.talktalkcoach.service;

import com.codit.talktalkcoach.domain.entity.DailyWord;
import com.codit.talktalkcoach.domain.entity.User;
import com.codit.talktalkcoach.domain.enums.SpeechStatus;
import com.codit.talktalkcoach.domain.enums.TargetLevel;
import com.codit.talktalkcoach.external.gpt.GptClient;
import com.codit.talktalkcoach.repository.DailyWordRepository;
import com.codit.talktalkcoach.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class DailyQuizSchedulerService {

    private final DailyWordRepository dailyWordRepository;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${openai.api.key}")
    private String apiKey;

    private static final String GPT_URL = "https://api.openai.com/v1/chat/completions";
    private static final String MODEL   = "gpt-4o-mini";

    /**
     * 매일 00:05에 레벨별 데일리 퀴즈 3개 생성
     * 레벨당 3개씩 총 5레벨 = 15개 퀴즈 생성
     */
    @Scheduled(cron = "0 5 0 * * *", zone = "Asia/Seoul")
    @Transactional
    public void generateDailyQuiz() {
        log.info("=== 데일리 퀴즈 생성 시작 ===");

        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        LocalDateTime todayEnd   = todayStart.plusDays(1);

        // 오늘 이전 전체 퀴즈 삭제 (누적 방지)
        LocalDateTime removeAll = todayStart; // 오늘 00:00 이전 종류된 퀴즈 전당
        dailyWordRepository.deleteOldQuiz(removeAll);

        // 레벨별 퀴즈 생성
        for (TargetLevel level : TargetLevel.values()) {
            try {
                generateQuizForLevel(level);
                log.info("레벨 [{}] 퀴즈 생성 완료", level);
            } catch (Exception e) {
                log.error("레벨 [{}] 퀴즈 생성 실패: {}", level, e.getMessage());
            }
        }

        log.info("=== 데일리 퀴즈 생성 완료 ===");
    }

    /**
     * 특정 레벨 퀴즈 3개 생성 (수동 호출용)
     */
    @Transactional
    public void generateQuizForLevel(TargetLevel level) throws Exception {
        // 오늘 해당 레벨 퀴즈 먼저 삭제 (중복 누적 방지)
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        LocalDateTime todayEnd   = todayStart.plusDays(1);
        dailyWordRepository.deleteTodayQuizByLevel(level, todayStart, todayEnd);
        log.info("[{}] 기존 퀴즈 삭제 완료", level);

        String prompt = buildQuizPrompt(level);

        Map<String, Object> body = Map.of(
                "model", MODEL,
                "messages", List.of(
                        Map.of("role", "system", "content", buildSystemPrompt()),
                        Map.of("role", "user",   "content", prompt)
                ),
                "response_format", Map.of("type", "json_object"),
                "temperature", 0.8,
                "max_completion_tokens", 1000
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<Map> response = restTemplate.exchange(
                GPT_URL, HttpMethod.POST, new HttpEntity<>(body, headers), Map.class);

        String content = extractContent(response.getBody());
        log.debug("[Quiz GPT 응답] level={}, content={}", level, content);

        parseAndSaveQuizzes(content, level);
    }

    // ─── 시스템 프롬프트 ────────────────────────────────────────────────────────
    private String buildSystemPrompt() {
        return """
                당신은 한국어 어휘 교육 전문가입니다.
                
                다음 연구 기준을 반드시 따릅니다:
                - 2023 국어 기초어휘 선정 및 어휘 등급화 연구 (국립국어원)
                - 서울대학교 국어교육연구소(2012) 어휘 평가 내용 연구
                
                위 연구의 학년별 어휘 등급 기준에 따라,
                각 학습자 수준에 적합한 어휘를 선정하여 퀴즈를 출제합니다.
                
                퀴즈 출제 원칙:
                1. 해당 수준 학습자가 스피치에서 자주 틀리거나 혼동하는 어휘
                2. 뜻풀이는 국어사전 기준으로 정확하게
                3. 오답 선택지는 정답과 의미가 비슷하거나 혼동하기 쉬운 단어
                4. 문장은 해당 수준 학습자가 실제 스피치에서 사용할 법한 맥락
                5. 반드시 한국어 어휘만 출제 (영어 단어 제외)
                """;
    }

    // ─── 레벨별 사용자 프롬프트 ────────────────────────────────────────────────
    private String buildQuizPrompt(TargetLevel level) {
        String levelDesc = getLevelDescription(level);
        String levelGuide = getLevelGuide(level);

        return String.format("""
                [%s] 수준 학습자를 위한 한국어 어휘 퀴즈 3개를 생성해주세요.
                
                ## 수준 가이드
                %s
                
                ## 출력 형식 (JSON, 이 형식 외 다른 텍스트 금지)
                {
                  "quizzes": [
                    {
                      "word": "정답 어휘",
                      "description": "______에 들어갈 알맞은 단어를 고르세요.\\n\\n[예문] 이 발표는 논리적으로 이루어져 청중을 ______시켰다.",
                      "answer": "정답 선택지",
                      "option2": "오답1",
                      "option3": "오답2"
                    },
                    { ... },
                    { ... }
                  ]
                }
                
                ## 규칙
                - word: 정답 어휘 (1~4음절 한국어 단어)
                - description: 빈칸(______) 포함한 예문. 단어의 뜻풀이를 첫 줄에 쓰고 두 번째 줄에 예문.
                - answer: 정답 (word와 동일하거나 활용형)
                - option2, option3: 오답 2개 (정답과 유사하지만 다른 단어)
                - 3개의 퀴즈는 서로 다른 어휘 영역에서 출제
                """, levelDesc, levelGuide);
    }

    private String getLevelDescription(TargetLevel level) {
        return switch (level) {
            case ELEM_1_2   -> "초등학교 1~2학년";
            case ELEM_3_4   -> "초등학교 3~4학년";
            case ELEM_5_6   -> "초등학교 5~6학년";
            case MIDDLE_1_2 -> "중학교 1~2학년";
            case MIDDLE_3   -> "중학교 3학년";
        };
    }

    private String getLevelGuide(TargetLevel level) {
        return switch (level) {
            case ELEM_1_2 -> """
                    - 기초 생활 어휘, 일상 단어 위주
                    - 2~3음절의 쉬운 단어
                    - 예: 관계, 순서, 이유, 결과, 느낌 등의 기초 어휘
                    - 오답도 비슷한 수준의 쉬운 단어로 구성""";
            case ELEM_3_4 -> """
                    - 교과서 기본 어휘 수준
                    - 원인-결과, 주장-이유 관련 어휘
                    - 예: 근거, 주장, 설명, 원인, 변화 등
                    - 오답은 의미가 비슷하여 혼동하기 쉬운 단어""";
            case ELEM_5_6 -> """
                    - 교과서 심화 어휘, 추상 개념 어휘
                    - 발표·설명에 쓰이는 접속 표현 어휘
                    - 예: 체계적, 구성, 분류, 전개, 논리 등
                    - 오답은 한자어 계열 유사 단어""";
            case MIDDLE_1_2 -> """
                    - 중학교 수준 개념어·사고도구어
                    - 논술·발표에 활용되는 논리 어휘
                    - 예: 타당성, 설득, 근거, 반론, 논증 등
                    - 오답은 유사한 의미의 한자어""";
            case MIDDLE_3 -> """
                    - 고급 추상어, 논리어, 수사적 표현
                    - 토론·설득 관련 전문 어휘
                    - 예: 반박, 쟁점, 역설, 논거, 전제 등
                    - 오답은 의미가 미묘하게 다른 고급 어휘""";
        };
    }

    // ─── GPT 응답 파싱 및 저장 ─────────────────────────────────────────────────
    private void parseAndSaveQuizzes(String content, TargetLevel level) throws Exception {
        JsonNode root = objectMapper.readTree(content);
        JsonNode quizzes = root.path("quizzes");

        if (!quizzes.isArray()) {
            log.error("quizzes 필드가 배열이 아님: {}", content);
            return;
        }

        for (JsonNode quiz : quizzes) {
            DailyWord word = DailyWord.builder()
                    .word(quiz.path("word").asText())
                    .description(quiz.path("description").asText())
                    .answer(quiz.path("answer").asText())
                    .option2(quiz.path("option2").asText())
                    .option3(quiz.path("option3").asText())
                    .targetLevel(level)
                    .build();
            dailyWordRepository.save(word);
        }
    }

    @SuppressWarnings("unchecked")
    private String extractContent(Map<?, ?> responseBody) {
        return (String) ((Map<?, ?>) ((Map<?, ?>) ((List<?>) responseBody
                .get("choices")).get(0)).get("message")).get("content");
    }
}
