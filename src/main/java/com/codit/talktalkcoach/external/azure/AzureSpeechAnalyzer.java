package com.codit.talktalkcoach.external.azure;

import com.codit.talktalkcoach.external.azure.dto.AzureSpeechResponse;

/**
 * Azure Speech 분석 추상화 인터페이스
 *
 * - 로컬/테스트: MockAzureSpeechClient  (@Profile("local"))
 * - 운영:       AzureSpeechClient       (@Profile("prod"))
 *
 * Azure 연동 준비가 되면 application.yml 의 spring.profiles.active 를
 * "local" → "prod" 로 바꾸는 것만으로 실제 SDK로 전환됩니다.
 */
public interface AzureSpeechAnalyzer {
    AzureSpeechResponse analyze(byte[] audioData, String language);
}
