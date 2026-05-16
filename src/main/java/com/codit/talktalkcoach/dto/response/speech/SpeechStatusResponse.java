package com.codit.talktalkcoach.dto.response.speech;

import com.codit.talktalkcoach.domain.enums.SpeechStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SpeechStatusResponse {
    private Long speechId;
    private SpeechStatus status;
    private String message;

    public static SpeechStatusResponse processing(Long speechId) {
        return SpeechStatusResponse.builder()
                .speechId(speechId)
                .status(SpeechStatus.PROCESSING)
                .message("분석이 진행 중입니다. 잠시 후 다시 확인해주세요.")
                .build();
    }

    public static SpeechStatusResponse completed(Long speechId) {
        return SpeechStatusResponse.builder()
                .speechId(speechId)
                .status(SpeechStatus.COMPLETED)
                .message("분석이 완료되었습니다.")
                .build();
    }

    public static SpeechStatusResponse failed(Long speechId) {
        return SpeechStatusResponse.builder()
                .speechId(speechId)
                .status(SpeechStatus.FAILED)
                .message("분석에 실패했습니다. 다시 시도해주세요.")
                .build();
    }
}
