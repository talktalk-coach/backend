package com.codit.talktalkcoach.domain.enums;

/**
 * 스피치 유형 (GptClient 프롬프트와 1:1 대응)
 * - PRESENTATION : 발표형 (정보 전달) — 모든 레벨 허용
 * - SPEECH       : 설득·연설형        — ELEM_5_6 이상
 * - DEBATE       : 토론·논증형        — ELEM_5_6 이상
 *
 * ELEM_1_2, ELEM_3_4 는 PRESENTATION 만 허용 (GptClient 내부에서 자동 보정)
 */
public enum SpeechCategory {
    PRESENTATION,
    SPEECH,
    DEBATE
}
