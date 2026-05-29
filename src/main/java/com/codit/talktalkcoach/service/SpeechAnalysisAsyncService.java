package com.codit.talktalkcoach.service;

import com.codit.talktalkcoach.domain.entity.Speech;
import com.codit.talktalkcoach.domain.entity.SpeechAnalysis;
import com.codit.talktalkcoach.exception.BusinessException;
import com.codit.talktalkcoach.exception.ErrorCode;
import com.codit.talktalkcoach.external.azure.AzureSpeechClient;
import com.codit.talktalkcoach.external.azure.dto.AzureSpeechResponse;
import com.codit.talktalkcoach.external.gpt.GptClient;
import com.codit.talktalkcoach.external.gpt.dto.GptAnalysisResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.codit.talktalkcoach.repository.SpeechAnalysisRepository;
import com.codit.talktalkcoach.repository.SpeechRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpeechAnalysisAsyncService {

    private final AzureSpeechClient azureSpeechClient;
    private final GptClient gptClient;
    private final SpeechAnalysisRepository speechAnalysisRepository;
    private final SpeechRepository speechRepository;
    private final DashboardService dashboardService;
    private final ObjectMapper objectMapper;

    @Async
    public void analyzeAsync(Long speechId, byte[] audioBytes) {
        log.info("=== 비동기 분석 시작 === speechId: {}, 데이터: {}KB",
                speechId, audioBytes.length / 1024);

        // ✅ 분석 + 저장을 별도 메서드(별도 트랜잭션)로 분리
        //    대시보드 갱신 실패가 분석 저장을 롤백하지 않도록
        boolean success = saveAnalysisInNewTransaction(speechId, audioBytes);

        if (success) {
            // 대시보드 갱신은 분석 저장과 독립된 별도 작업
            try {
                Speech speech = speechRepository.findById(speechId).orElse(null);
                if (speech != null) {
                    dashboardService.refreshStats(speech.getUser());
                }
            } catch (Exception e) {
                // 대시보드 갱신 실패는 분석 결과에 영향 없음
                log.warn("대시보드 갱신 실패 (분석 결과는 정상 저장됨): {}", e.getMessage());
            }
        }
    }

    /**
     * 분석 저장 전용 트랜잭션
     * 이 메서드 안에서 예외 발생 시 speech.fail() 만 커밋되고
     * 대시보드 갱신은 영향받지 않음
     */
    @Transactional
    public boolean saveAnalysisInNewTransaction(Long speechId, byte[] audioBytes) {
        Speech speech = speechRepository.findById(speechId)
                .orElseThrow(() -> new BusinessException(ErrorCode.SPEECH_NOT_FOUND));

        try {
            // 1. Azure 발음 평가
            AzureSpeechResponse azureResult = azureSpeechClient.analyze(audioBytes, "ko-KR");
            log.info("Azure 완료 - transcript 길이: {}자", azureResult.getTranscript().length());

            if (azureResult.getTranscript() == null || azureResult.getTranscript().isBlank()) {
                log.warn("transcript 비어있음 — 음성 인식 실패");
                speech.fail();
                speechRepository.save(speech);
                return false;
            }

            // 2. GPT 분석
            GptAnalysisResult gptResult = gptClient.analyzeSpeech(
                    azureResult.getTranscript(),
                    speech.getTargetLevel(),
                    speech.getCategory()
            );
            log.info("GPT 완료 - vocabularyScore: {}", gptResult.getVocabularyScore());

            // Azure ko-KR prosody 미지원 시 GPT 텍스트 추정값으로 대체
            double finalProsodyScore = (azureResult.getProsodyScore() != null
                    && azureResult.getProsodyScore() > 0)
                    ? azureResult.getProsodyScore()
                    : (gptResult.getProsodyScore() != null ? gptResult.getProsodyScore() : 0.0);
            log.info("[prosody] Azure={}, GPT={}, 최종={}",
                    azureResult.getProsodyScore(), gptResult.getProsodyScore(), finalProsodyScore);

            // 3. 결과 저장
            SpeechAnalysis analysis = SpeechAnalysis.builder()
                    .speech(speech)
                    .transcript(azureResult.getTranscript())
                    .accuracyScore(azureResult.getAccuracyScore())
                    .fluencyScore(azureResult.getFluencyScore())
                    .prosodyScore(finalProsodyScore)
                    .vocabularyScore(gptResult.getVocabularyScore())
                    .wordCount(gptResult.getWordCount())
                    .logicScore(gptResult.getLogicScore())
                    .structureScore(gptResult.getStructureScore())
                    .vocabularyFeedback(gptResult.getVocabularyFeedback())
                    .sentenceStructureFeedback(gptResult.getSentenceStructureFeedback())
                    .logicFeedback(gptResult.getLogicFeedback())
                    .overallFeedback(gptResult.getOverallFeedback())
                    .customPlan(serializeCustomPlan(gptResult))
                    .wordLevelData(Map.of("words", azureResult.getWords()))
                    .build();

            speechAnalysisRepository.save(analysis);

            // GPT가 생성한 주제로 title 덮어씀 (5단어 이내 요약)
            if (gptResult.getTitle() != null && !gptResult.getTitle().isBlank()) {
                speech.updateTitle(gptResult.getTitle());
            }

            speech.complete();
            speechRepository.save(speech);

            log.info("=== 분석 완료 및 저장 성공 === speechId: {}", speechId);
            return true;

        } catch (Exception e) {
            log.error("=== 분석 실패 === speechId: {}", speechId);
            log.error("type: {}, message: {}", e.getClass().getName(), e.getMessage());
            log.error("stacktrace:", e);
            speech.fail();
            speechRepository.save(speech);
            return false;
        }
    }

    private String serializeCustomPlan(GptAnalysisResult result) {
        if (result.getCustomPlan() == null || result.getCustomPlan().isEmpty()) return null;
        try {
            return objectMapper.writeValueAsString(result.getCustomPlan());
        } catch (Exception e) {
            log.warn("customPlan 직렬화 실패: {}", e.getMessage());
            return result.getCustomPlan().toString();
        }
    }
}
