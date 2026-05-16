-- ============================================================
--  TalkTalk Coach — 스키마 마이그레이션
--  TargetLevel 변경 + speeches.category 컬럼 추가
-- ============================================================

-- 1. speeches 테이블에 category 컬럼 추가
ALTER TABLE `speeches`
    ADD COLUMN `category` VARCHAR(20) NOT NULL DEFAULT 'PRESENTATION'
        COMMENT 'PRESENTATION | SPEECH | DEBATE'
    AFTER `target_level`;

-- 2. target_level 컬럼 길이 확인 (MIDDLE_1_2 = 10자, VARCHAR(20) 이면 OK)
--    기존 데이터가 있다면 아래 UPDATE로 기존 값을 새 Enum에 맞게 변환
UPDATE `users` SET `target_level` = 'ELEM_1_2'   WHERE `target_level` = 'ELEMENTARY_LOW';
UPDATE `users` SET `target_level` = 'ELEM_3_4'   WHERE `target_level` = 'ELEMENTARY_LOW';
UPDATE `users` SET `target_level` = 'ELEM_5_6'   WHERE `target_level` = 'ELEMENTARY_HIGH';
UPDATE `users` SET `target_level` = 'MIDDLE_1_2' WHERE `target_level` = 'MIDDLE';
UPDATE `users` SET `target_level` = 'MIDDLE_3'   WHERE `target_level` = 'HIGH';
UPDATE `users` SET `target_level` = 'MIDDLE_1_2' WHERE `target_level` = 'FOREIGN';

UPDATE `speeches` SET `target_level` = 'ELEM_1_2'   WHERE `target_level` = 'ELEMENTARY_LOW';
UPDATE `speeches` SET `target_level` = 'ELEM_5_6'   WHERE `target_level` = 'ELEMENTARY_HIGH';
UPDATE `speeches` SET `target_level` = 'MIDDLE_1_2' WHERE `target_level` = 'MIDDLE';
UPDATE `speeches` SET `target_level` = 'MIDDLE_3'   WHERE `target_level` = 'HIGH';
UPDATE `speeches` SET `target_level` = 'MIDDLE_1_2' WHERE `target_level` = 'FOREIGN';

UPDATE `daily_words` SET `target_level` = 'ELEM_1_2'   WHERE `target_level` = 'ELEMENTARY_LOW';
UPDATE `daily_words` SET `target_level` = 'ELEM_5_6'   WHERE `target_level` = 'ELEMENTARY_HIGH';
UPDATE `daily_words` SET `target_level` = 'MIDDLE_1_2' WHERE `target_level` = 'MIDDLE';
UPDATE `daily_words` SET `target_level` = 'MIDDLE_3'   WHERE `target_level` = 'HIGH';
UPDATE `daily_words` SET `target_level` = 'MIDDLE_1_2' WHERE `target_level` = 'FOREIGN';

-- year_month 컬럼명을 stat_month로 변경
ALTER TABLE monthly_stats
    CHANGE COLUMN year_month stat_month VARCHAR(7) NOT NULL;

-- 기존 유니크 인덱스 제거 후 재생성
ALTER TABLE monthly_stats
DROP INDEX IF EXISTS uq_monthly_stats_user_month;

ALTER TABLE monthly_stats
    ADD CONSTRAINT uq_monthly_stats_user_stat_month
        UNIQUE (user_id, stat_month);

USE talktalkcoach;
-- 대안: 새 컬럼 추가 → 데이터 복사 → 기존 컬럼 삭제
ALTER TABLE monthly_stats ADD COLUMN stat_month VARCHAR(7) NOT NULL DEFAULT '';
UPDATE monthly_stats SET stat_month = year_month;
ALTER TABLE monthly_stats DROP COLUMN year_month;
ALTER TABLE monthly_stats ADD CONSTRAINT uq_monthly_stats_user_stat_month UNIQUE (user_id, stat_month);