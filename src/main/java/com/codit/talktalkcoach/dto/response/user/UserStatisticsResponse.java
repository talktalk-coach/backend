package com.codit.talktalkcoach.dto.response.user;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@Builder
public class UserStatisticsResponse {

    private String summaryLine;
    private double growthRate;
    private Map<String, Double> masteryMap;
    private List<DailyScoreDto> dailyScores;

    @Getter
    @Builder
    public static class DailyScoreDto {
        private String date;
        // completenessScore, sentenceScore 제거
        private Double accuracyScore;
        private Double fluencyScore;
        private Double prosodyScore;
        private Double vocabularyScore;
        private Double logicScore;
        private Double structureScore;
        private Double averageScore;
    }

    @Getter
    @Builder
    public static class LevelGrowthDto {
        private String targetLevel;
        private List<Double> scores;
    }
}
