package com.codit.talktalkcoach.dto.request.speech;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SpeechDurationRequest {

    @NotBlank
    private String jobId;   // Azure 비동기 jobId

    private int durationSeconds;
}
