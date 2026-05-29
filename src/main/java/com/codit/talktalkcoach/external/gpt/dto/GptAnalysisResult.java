package com.codit.talktalkcoach.external.gpt.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GptAnalysisResult {

    private String scoringRationale;

    @Setter
    private String detectedCategory;

    // 스피치 주제 요약 — GPT가 생성, Speech.title을 덮어쀸 (예: "독서의 중요성")
    private String title;

    // sentenceScore 제거
    private Double vocabularyScore;
    private Double logicScore;
    private Double structureScore;

    // Azure ko-KR에서 prosody 미지원 → GPT 텍스트 기반 간접 추정
    private Double prosodyScore;

    @Setter
    private Integer wordCount;

    private String vocabularyFeedback;
    private String sentenceStructureFeedback;
    private String logicFeedback;
    private String overallFeedback;

    private List<ImprovementItem> customPlan;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ImprovementItem {
        private String title;
        private String description;
        private String category;
    }
}
