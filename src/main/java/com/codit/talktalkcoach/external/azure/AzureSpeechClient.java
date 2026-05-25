package com.codit.talktalkcoach.external.azure;

import com.codit.talktalkcoach.exception.BusinessException;
import com.codit.talktalkcoach.exception.ErrorCode;
import com.codit.talktalkcoach.external.azure.dto.AzureSpeechResponse;

import com.microsoft.cognitiveservices.speech.audio.AudioConfig;
import com.microsoft.cognitiveservices.speech.CancellationReason;
import com.microsoft.cognitiveservices.speech.PronunciationAssessmentConfig;
import com.microsoft.cognitiveservices.speech.PronunciationAssessmentGradingSystem;
import com.microsoft.cognitiveservices.speech.PronunciationAssessmentGranularity;
import com.microsoft.cognitiveservices.speech.PronunciationAssessmentResult;
import com.microsoft.cognitiveservices.speech.ResultReason;
import com.microsoft.cognitiveservices.speech.SpeechConfig;
import com.microsoft.cognitiveservices.speech.SpeechRecognizer;
import com.microsoft.cognitiveservices.speech.SpeechRecognitionResult;
import com.microsoft.cognitiveservices.speech.PropertyId;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

@Slf4j
@Component
public class AzureSpeechClient {

    @Value("${azure.speech.key}")
    private String subscriptionKey;

    @Value("${azure.speech.region}")
    private String region;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public AzureSpeechResponse analyze(byte[] audioData, String language) {

        Path tempFile = null;
        try {
            tempFile = Files.createTempFile("talktalk_speech_", ".wav");
            Files.write(tempFile, audioData);
            log.info("임시 WAV 파일 생성: {} ({}KB)", tempFile, audioData.length / 1024);
        } catch (IOException e) {
            log.error("임시 파일 생성 실패: {}", e.getMessage());
            throw new BusinessException(ErrorCode.AZURE_API_ERROR);
        }

        SpeechConfig speechConfig = SpeechConfig.fromSubscription(subscriptionKey, region);
        speechConfig.setSpeechRecognitionLanguage(language);
        speechConfig.requestWordLevelTimestamps();

        PronunciationAssessmentConfig pronunciationConfig;
        try {
            pronunciationConfig = new PronunciationAssessmentConfig(
                    "",
                    PronunciationAssessmentGradingSystem.HundredMark,
                    PronunciationAssessmentGranularity.Word,
                    false
            );
            pronunciationConfig.enableProsodyAssessment();
        } catch (Exception e) {
            log.error("PronunciationAssessmentConfig 생성 실패: {}", e.getMessage());
            deleteTempFile(tempFile);
            throw new BusinessException(ErrorCode.AZURE_API_ERROR);
        }

        AudioConfig audioConfig = AudioConfig.fromWavFileInput(tempFile.toString());

        Semaphore stopSemaphore = new Semaphore(0);

        StringBuilder             fullTranscript     = new StringBuilder();
        List<Map<String, Object>> allWords           = new ArrayList<>();
        List<Double>              fluencyScores      = new ArrayList<>();
        List<Double>              prosodyScores      = new ArrayList<>();
        List<Double>              accuracyScores     = new ArrayList<>();
        boolean[]                 hasError           = {false};

        try (SpeechRecognizer recognizer = new SpeechRecognizer(speechConfig, audioConfig)) {
            pronunciationConfig.applyTo(recognizer);

            recognizer.recognized.addEventListener((s, e) -> {
                if (e.getResult().getReason() == ResultReason.RecognizedSpeech) {

                    fullTranscript.append(e.getResult().getText()).append(" ");

                    PronunciationAssessmentResult pr =
                            PronunciationAssessmentResult.fromResult(e.getResult());

                    accuracyScores.add(pr.getAccuracyScore());
                    fluencyScores.add(pr.getFluencyScore());

                    double prosody = parseProsodyFromJson(e.getResult());
                    prosodyScores.add(prosody);

                    log.info("[Azure 세그먼트] accuracy={}, fluency={}, prosody={}",
                            pr.getAccuracyScore(), pr.getFluencyScore(), prosody);

                    parseWordLevelData(e.getResult(), allWords);

                } else if (e.getResult().getReason() == ResultReason.NoMatch) {
                    log.warn("NoMatch: 세그먼트 인식 실패");
                }
            });

            recognizer.canceled.addEventListener((s, e) -> {
                if (e.getReason() == CancellationReason.Error) {
                    log.error("Azure 취소 — ErrorCode: {}, Details: {}",
                            e.getErrorCode(), e.getErrorDetails());
                    hasError[0] = true;
                }
                stopSemaphore.release();
            });

            recognizer.sessionStopped.addEventListener((s, e) -> {
                log.info("Azure 세션 정상 종료");
                stopSemaphore.release();
            });

            recognizer.startContinuousRecognitionAsync().get();
            stopSemaphore.acquire();
            recognizer.stopContinuousRecognitionAsync().get();

        } catch (Exception e) {
            log.error("Azure 연속 인식 실패: {}", e.getMessage());
            deleteTempFile(tempFile);
            throw new BusinessException(ErrorCode.AZURE_API_ERROR);
        } finally {
            deleteTempFile(tempFile);
        }

        if (hasError[0]) {
            throw new BusinessException(ErrorCode.AZURE_API_ERROR);
        }

        return buildFinalResponse(
                fullTranscript.toString().trim(),
                allWords, fluencyScores, prosodyScores, accuracyScores);
    }

    // ─── prosodyScore: JSON에서 직접 파싱 ────────────────────────────────
    private double parseProsodyFromJson(SpeechRecognitionResult result) {
        try {
            String json = result.getProperties()
                    .getProperty(PropertyId.SpeechServiceResponse_JsonResult);
            if (json == null || json.isEmpty()) return 0.0;

            JsonNode root  = MAPPER.readTree(json);
            JsonNode nBest = root.path("NBest");
            if (!nBest.isArray() || nBest.isEmpty()) return 0.0;

            JsonNode first = nBest.get(0);

            double prosody = first.path("ProsodyScore").asDouble(-1);
            if (prosody >= 0) { log.info("[Prosody 찾음] ProsodyScore={}", prosody); return prosody; }

            prosody = first.path("PronunciationAssessment").path("ProsodyScore").asDouble(-1);
            if (prosody >= 0) { log.info("[Prosody 찾음] PronunciationAssessment.ProsodyScore={}", prosody); return prosody; }

            prosody = first.path("PronScore").asDouble(-1);
            if (prosody >= 0) { log.info("[Prosody] PronScore={} (대체)", prosody); return prosody; }

            log.warn("[Prosody] 모든 필드를 탐색했으나 찾지 못함");
            return 0.0;

        } catch (Exception e) {
            log.warn("prosody JSON 파싱 실패: {}", e.getMessage());
            return 0.0;
        }
    }

    // ─── word-level 데이터 파싱 ───────────────────────────────────────────
    private void parseWordLevelData(SpeechRecognitionResult result,
                                    List<Map<String, Object>> allWords) {
        try {
            String json = result.getProperties()
                    .getProperty(PropertyId.SpeechServiceResponse_JsonResult);
            if (json == null || json.isEmpty()) return;

            JsonNode root  = MAPPER.readTree(json);
            JsonNode nBest = root.path("NBest");
            if (!nBest.isArray() || nBest.isEmpty()) return;

            JsonNode words = nBest.get(0).path("Words");
            for (JsonNode wordNode : words) {
                Map<String, Object> wordMap = new HashMap<>();
                wordMap.put("word",        wordNode.path("Word").asText(""));
                JsonNode pa = wordNode.path("PronunciationAssessment");
                wordMap.put("accuracyScore", pa.path("AccuracyScore").asDouble(0.0));
                wordMap.put("errorType",     pa.path("ErrorType").asText("None"));
                wordMap.put("offsetMs",      wordNode.path("Offset").asLong(0)   / 10_000L);
                wordMap.put("durationMs",    wordNode.path("Duration").asLong(0) / 10_000L);
                allWords.add(wordMap);
            }
        } catch (Exception e) {
            log.warn("Word-level 파싱 실패: {}", e.getMessage());
        }
    }

    // ─── 최종 점수 계산 ───────────────────────────────────────────────────
    private AzureSpeechResponse buildFinalResponse(
            String transcript,
            List<Map<String, Object>> words,
            List<Double> fluencyScores,
            List<Double> prosodyScores,
            List<Double> accuracyScores) {

        double accuracyScore = safeAverage(accuracyScores);
        double fluencyScore  = safeAverage(fluencyScores);
        double prosodyScore  = safeAverage(prosodyScores);

        log.info("[Azure 최종] accuracy={}, fluency={}, prosody={}",
                accuracyScore, fluencyScore, prosodyScore);

        return AzureSpeechResponse.builder()
                .transcript(transcript)
                .accuracyScore(accuracyScore)
                .fluencyScore(fluencyScore)
                .prosodyScore(prosodyScore)
                .words(words)
                .build();
    }

    private double safeAverage(List<Double> list) {
        if (list == null || list.isEmpty()) return 0.0;
        return list.stream()
                .filter(v -> v != null)
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }

    private void deleteTempFile(Path path) {
        if (path != null) {
            try {
                Files.deleteIfExists(path);
                log.debug("임시 파일 삭제: {}", path);
            } catch (IOException e) {
                log.warn("임시 파일 삭제 실패: {}", e.getMessage());
            }
        }
    }
}
