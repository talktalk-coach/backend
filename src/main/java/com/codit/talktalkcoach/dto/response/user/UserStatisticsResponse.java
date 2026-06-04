package com.codit.talktalkcoach.dto.response.user;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@Builder
public class UserStatisticsResponse {

    private String summaryTitle;   // 예: "유창성과 발음이 눈에 띄게 성장했어요!"
    private String summaryDetail;  // 예: "지난 연습 대비 유창성 점수가 13.9% 향상되었습니다."
    private double growthRate;
    private Map<String, Double> masteryMap;
    private List<DailyScoreDto> dailyScores;
    private TotalScoreDto totalScores;  // 전체 스피치 항목별 평균

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

    @Getter
    @Builder
    public static class TotalScoreDto {
        private Double accuracyScore;
        private Double fluencyScore;
        private Double prosodyScore;
        private Double vocabularyScore;
        private Double logicScore;
        private Double structureScore;
        private Double averageScore;
    }
}
