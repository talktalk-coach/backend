package com.codit.talktalkcoach.external.azure;

import com.codit.talktalkcoach.external.azure.dto.AzureSpeechResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Azure Speech SDK Mock 구현체
 *
 * Azure API 연동 전 로컬 테스트용으로 사용합니다.
 * - 실제 API 호출 없이 고정된 더미 데이터를 반환합니다.
 * - spring.profiles.active = local 일 때만 Bean으로 등록됩니다.
 *
 * TODO: Azure 연동 준비 완료 시
 *   1. build.gradle Azure SDK 의존성 주석 해제
 *   2. application.yml active 프로필을 "prod" 로 변경
 *   → 이 클래스는 자동으로 비활성화되고 AzureSpeechClient 가 주입됩니다.
 */
@Slf4j
@Component
@Profile("local")
public class MockAzureSpeechClient implements AzureSpeechAnalyzer {

    @Override
    public AzureSpeechResponse analyze(byte[] audioData, String language) {
        log.info("[MOCK] Azure Speech API 호출 생략 — 더미 데이터 반환 (언어: {})", language);

        // 더미 전사 텍스트 (실제 오디오 내용과 무관)
        String dummyTranscript =
                "안녕하세요. 오늘 제가 발표할 주제는 환경 보호의 중요성입니다. " +
                "우리가 일상에서 실천할 수 있는 작은 행동들이 지구를 지키는 데 큰 도움이 됩니다. " +
                "예를 들어 분리수거를 철저히 하고 일회용품 사용을 줄이는 것이 있습니다. " +
                "우리 모두 함께 노력한다면 더 나은 미래를 만들 수 있습니다.";

        // 더미 word-level 데이터
        List<Map<String, Object>> dummyWords = List.of(
                Map.of("word", "안녕하세요", "accuracyScore", 95.0, "errorType", "None", "offsetMs", 0,    "durationMs", 600),
                Map.of("word", "오늘",      "accuracyScore", 98.0, "errorType", "None", "offsetMs", 700,  "durationMs", 300),
                Map.of("word", "제가",      "accuracyScore", 92.0, "errorType", "None", "offsetMs", 1100, "durationMs", 300),
                Map.of("word", "발표할",    "accuracyScore", 88.0, "errorType", "None", "offsetMs", 1500, "durationMs", 400),
                Map.of("word", "주제는",    "accuracyScore", 90.0, "errorType", "None", "offsetMs", 2000, "durationMs", 400)
        );

        return AzureSpeechResponse.builder()
                .transcript(dummyTranscript)
                .accuracyScore(88.5)        // 정확도
                .fluencyScore(82.0)         // 유창성
                .prosodyScore(79.5)         // 운율
                .words(dummyWords)
                .build();
    }
}
