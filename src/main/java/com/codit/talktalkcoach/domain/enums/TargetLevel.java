package com.codit.talktalkcoach.domain.enums;

/**
 * 2022 개정 국어과 교육과정 학년군 기반 레벨 체계
 * GptClient 프롬프트와 1:1 대응 (절대 임의 변경 금지)
 */
public enum TargetLevel {
    ELEM_1_2,    // 초등학교 1~2학년 [2국01 기반]
    ELEM_3_4,    // 초등학교 3~4학년 [4국01 기반]
    ELEM_5_6,    // 초등학교 5~6학년 [6국01 기반]
    MIDDLE_1_2,  // 중학교 1~2학년   [9국01 기반]
    MIDDLE_3     // 중학교 3학년     [9국01 심화]
}
