package com.codit.talktalkcoach.dto.response.user;

import com.codit.talktalkcoach.domain.enums.TargetLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * 성장치 히스토리 응답
 * 레벨별로 스피치 회차 순서 + 날짜 + averageScore를 반환
 */
@Getter
@Builder
public class GrowthHistoryResponse {

    /** 레벨 코드 (ELEM_1_2 | ELEM_3_4 | ELEM_5_6 | MIDDLE_1_2 | MIDDLE_3) */
    private TargetLevel targetLevel;

    /** 레벨 표시명 (프론트 차트 범례용) */
    private String levelLabel;

    /** 해당 레벨로 진행한 스피치 목록 (날짜 오름차순) */
    private List<ScorePoint> scores;

    @Getter
    @Builder
    public static class ScorePoint {
        /** 해당 레벨 내 회차 번호 (1부터 시작) */
        private int index;

        /** 스피치 날짜 (yyyy-MM-dd) */
        private String date;

        /** 해당 스피치의 6개 항목 평균 점수 */
        private Double averageScore;
    }
}
