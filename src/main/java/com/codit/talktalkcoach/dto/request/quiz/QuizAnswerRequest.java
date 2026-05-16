package com.codit.talktalkcoach.dto.request.quiz;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class QuizAnswerRequest {

    @NotNull
    private Long wordId;

    @NotNull
    private String selectedAnswer;
}
