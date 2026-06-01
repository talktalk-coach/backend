package com.codit.talktalkcoach.dto.response.user;

import com.codit.talktalkcoach.domain.enums.TargetLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * 성장치 히스토리 응답
 * 레벨별 날짜 기준 평균 점수 int 배열 반환
 * - 하루에 여러 번 스피치한 경우 그날의 평균을 int로 반환
 * - 최신순 정렬 (앞 = 최근, 뒤 = 오래된 순)
 */
@Getter
@Builder
public class GrowthHistoryResponse {

    /** 레벨 코드 (ELEM_1_2 | ELEM_3_4 | ELEM_5_6 | MIDDLE_1_2 | MIDDLE_3) */
    private TargetLevel targetLevel;

    /** 레벨 표시명 (프론트 차트 범례용) */
    private String levelLabel;

    /**
     * 날짜별 평균 점수 int 배열 (최신순)
     * - 하루에 3번 스피치 → 그날의 평균 1개
     * - 소수점 없음 (반올림)
     * 예: [78, 74, 71, 68, 65]
     */
    private List<Integer> scores;
}
