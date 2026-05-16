package com.codit.talktalkcoach.dto.response.quiz;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QuizResultResponse {
    private boolean correct;
    private String correctAnswer;   // 틀렸을 때 정답을 보여주기 위함
    private String word;            // 정답 단어 (맞췄을 때 표시)
}
