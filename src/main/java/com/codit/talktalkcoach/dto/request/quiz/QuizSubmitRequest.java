package com.codit.talktalkcoach.dto.request.quiz;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class QuizSubmitRequest {

    @NotNull
    private Long wordId;

    @NotNull
    private String selectedOption; // 사용자가 선택한 텍스트
}
