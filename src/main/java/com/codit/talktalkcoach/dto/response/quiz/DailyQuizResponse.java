package com.codit.talktalkcoach.dto.response.quiz;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class DailyQuizResponse {

    private Long wordId;
    private String description;   // 퀴즈 문장 (빈칸 포함)
    private List<String> options; // 선택지 3개 (무작위 섞임)
    private boolean answered;     // 오늘 이미 풀었는지
    private Boolean isCorrect;    // 풀었다면 맞았는지 (안 풀었으면 null)
}
