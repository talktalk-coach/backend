package com.codit.talktalkcoach.dto.response.quiz;

import com.codit.talktalkcoach.domain.entity.DailyWord;
import lombok.Builder;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
@Builder
public class DailyWordResponse {
    private Long wordId;
    private String description;     // 단어 설명 (문제 지문)
    private List<String> options;   // 3가지 선택지 (셔플된 상태)

    public static DailyWordResponse from(DailyWord word) {
        List<String> options = Arrays.asList(
                word.getAnswer(), word.getOption2(), word.getOption3());
        Collections.shuffle(options);

        return DailyWordResponse.builder()
                .wordId(word.getWordId())
                .description(word.getDescription())
                .options(options)
                .build();
    }
}
