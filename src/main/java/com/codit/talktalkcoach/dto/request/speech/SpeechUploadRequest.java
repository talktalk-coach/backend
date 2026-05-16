package com.codit.talktalkcoach.dto.request.speech;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SpeechUploadRequest {

    @NotBlank(message = "스피치 제목을 입력해주세요.")
    private String title;
}
