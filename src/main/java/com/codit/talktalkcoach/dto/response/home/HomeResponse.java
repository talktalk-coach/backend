package com.codit.talktalkcoach.dto.response.home;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class HomeResponse {

    // 1. 레이더 차트: 최근 5개 스피치의 지표별 평균값 (단일 오각형)
    private RadarAverageDto radarAverage;

    // 2. 월간 평균 점수 목록
    private List<MonthlyScoreDto> monthlyScores;

    // 3. 오늘 스피치 진행 시간 (분 단위, 초 절삭)
    private int todayPracticeMinutes;

    // 4. AI 종합 피드백 (최근 5개 기반, GPT 생성)
    private String summaryFeedback;

    // 5. 누적 스피치 횟수
    private int totalCount;

    /**
     * 레이더 차트용 평균 DTO
     * 최근 5개 스피치의 각 지표 평균값
     */
    @Getter
    @Builder
    public static class RadarAverageDto {
        private int basedOnCount;      // 평균 계산에 사용된 스피치 수 (최대 5)
        private Double accuracyScore;
        private Double fluencyScore;
        private Double prosodyScore;
        private Double vocabularyScore;
        private Double logicScore;
        private Double structureScore;
        private Double averageScore;   // 6개 항목의 종합 평균
    }

    @Getter
    @Builder
    public static class MonthlyScoreDto {
        private String yearMonth;        // YYYY-MM
        private Double averageScore;
        private Integer practiceCount;
    }

    // 하위 호환: radarHistory는 더 이상 사용하지 않음 (radarAverage로 대체)
}
