package com.codit.talktalkcoach.util;

public class ScoreCalculator {

    private ScoreCalculator() {}

    /**
     * 8개 항목의 평균을 계산합니다. null 항목은 제외합니다.
     */
    public static double calculateAverage(Double... scores) {
        double sum = 0;
        int count = 0;
        for (Double score : scores) {
            if (score != null) {
                sum += score;
                count++;
            }
        }
        return count == 0 ? 0.0 : Math.round((sum / count) * 10.0) / 10.0;
    }

    /**
     * 성장률 계산: (최근 N개 평균 - 이전 N개 평균) / 이전 N개 평균 * 100
     */
    public static double calculateGrowthRate(double previousAvg, double recentAvg) {
        if (previousAvg == 0) return 0.0;
        return Math.round(((recentAvg - previousAvg) / previousAvg * 100) * 10.0) / 10.0;
    }
}
