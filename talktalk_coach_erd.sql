-- ============================================================
--  TalkTalk Coach — Full DDL
--  DB: MySQL 8.0+ / MariaDB 10.6+
-- ============================================================
CREATE DATABASE talktalkcoach_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- ──────────────────────────────────────────────
-- 1. users
-- ──────────────────────────────────────────────
CREATE TABLE `users` (
    `user_id`           BIGINT          NOT NULL AUTO_INCREMENT,
    `email`             VARCHAR(100)    NOT NULL,
    `password`          VARCHAR(255)    NULL        COMMENT '소셜 로그인 시 NULL',
    `nickname`          VARCHAR(50)     NOT NULL,
    `profile_image_url` VARCHAR(512)    NULL,
    `provider`          VARCHAR(20)     NOT NULL    DEFAULT 'LOCAL'  COMMENT 'LOCAL | GOOGLE | KAKAO',
    `target_level`      VARCHAR(20)     NULL        COMMENT 'FOREIGN | ELEMENTARY_LOW | ELEMENTARY_HIGH | MIDDLE | HIGH',
    `birth_date`        DATE            NULL        COMMENT '만 14세 미만 판별 및 학년 추천용',
    `is_under14`        BOOLEAN         NOT NULL    DEFAULT FALSE,
    `parent_email`      VARCHAR(100)    NULL        COMMENT '14세 미만 가입자의 보호자 이메일',
    `created_at`        TIMESTAMP       NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    `updated_at`        TIMESTAMP       NOT NULL    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted_at`        TIMESTAMP       NULL,
    PRIMARY KEY (`user_id`),
    UNIQUE KEY `uq_users_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='회원 정보';


-- ──────────────────────────────────────────────
-- 2. refresh_tokens
-- ──────────────────────────────────────────────
CREATE TABLE `refresh_tokens` (
    `token_id`      BIGINT          NOT NULL AUTO_INCREMENT,
    `user_id`       BIGINT          NOT NULL,
    `token_value`   VARCHAR(512)    NOT NULL,
    `expired_at`    TIMESTAMP       NOT NULL,
    PRIMARY KEY (`token_id`),
    KEY `idx_refresh_tokens_user_id` (`user_id`),
    CONSTRAINT `fk_refresh_tokens_user`
        FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='JWT 리프레시 토큰';


-- ──────────────────────────────────────────────
-- 3. email_verifications
--    type: SELF(본인) | PARENT(보호자)
-- ──────────────────────────────────────────────
CREATE TABLE `email_verifications` (
    `verification_id`   BIGINT          NOT NULL AUTO_INCREMENT,
    `user_id`           BIGINT          NULL        COMMENT '인증 요청 시점에 user가 없을 수 있으므로 NULL 허용',
    `email`             VARCHAR(100)    NOT NULL,
    `type`              VARCHAR(10)     NOT NULL    DEFAULT 'SELF'  COMMENT 'SELF | PARENT',
    `verification_code` VARCHAR(10)     NOT NULL,
    `is_verified`       BOOLEAN         NOT NULL    DEFAULT FALSE,
    `expired_at`        TIMESTAMP       NOT NULL,
    `created_at`        TIMESTAMP       NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`verification_id`),
    KEY `idx_email_verifications_user_id` (`user_id`),
    CONSTRAINT `fk_email_verifications_user`
        FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
        ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='이메일 인증 (본인/보호자)';


-- ──────────────────────────────────────────────
-- 4. speeches
-- ──────────────────────────────────────────────
CREATE TABLE `speeches` (
    `speech_id`     BIGINT          NOT NULL AUTO_INCREMENT,
    `user_id`       BIGINT          NOT NULL,
    `title`         VARCHAR(100)    NOT NULL,
    `audio_url`     VARCHAR(512)    NOT NULL,
    `duration`      INT             NOT NULL    COMMENT '녹음 길이(초)',
    `target_level`  VARCHAR(20)     NOT NULL    COMMENT '녹음 당시 사용자 학습 수준',
    `status`        VARCHAR(20)     NOT NULL    DEFAULT 'PROCESSING'  COMMENT 'PROCESSING | COMPLETED | FAILED',
    `created_at`    TIMESTAMP       NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    `deleted_at`    TIMESTAMP       NULL,
    PRIMARY KEY (`speech_id`),
    KEY `idx_speeches_user_id` (`user_id`),
    CONSTRAINT `fk_speeches_user`
        FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='스피치 녹음 파일 메타데이터';


-- ──────────────────────────────────────────────
-- 5. speech_analysis
--    Azure 4개 항목 + LLM 4개 항목 = 8개 점수
--    expression_score 제거, vocabulary → word_count 점수로 통일
-- ──────────────────────────────────────────────
CREATE TABLE `speech_analysis` (
    `analysis_id`                   BIGINT      NOT NULL AUTO_INCREMENT,
    `speech_id`                     BIGINT      NOT NULL,
    `transcript`                    TEXT        NULL        COMMENT 'Azure STT 전사 텍스트',

    -- Azure 평가 항목 (0~100)
    `accuracy_score`                DOUBLE      NULL        COMMENT '정확도',
    `fluency_score`                 DOUBLE      NULL        COMMENT '유창성',
    `completeness_score`            DOUBLE      NULL        COMMENT '완성도',
    `prosody_score`                 DOUBLE      NULL        COMMENT '운율',

    -- LLM 평가 항목 (0~100)
    `vocabulary_score`              DOUBLE      NULL        COMMENT '단어수 점수',
    `word_count`                    INT         NULL        COMMENT '실제 단어 수',
    `sentence_score`                DOUBLE      NULL        COMMENT '문법 점수',
    `logic_score`                   DOUBLE      NULL        COMMENT '논리 점수',
    `structure_score`               DOUBLE      NULL        COMMENT '문장구조 점수',

    -- LLM 피드백 텍스트
    `vocabulary_feedback`           TEXT        NULL,
    `sentence_structure_feedback`   TEXT        NULL,
    `logic_feedback`                TEXT        NULL,
    `overall_feedback`              TEXT        NULL        COMMENT '종합 피드백',
    `custom_plan`                   TEXT        NULL        COMMENT '맞춤 학습 계획',

    -- Azure word-level 상세 데이터
    `word_level_data`               JSON        NULL,

    `created_at`                    TIMESTAMP   NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`analysis_id`),
    UNIQUE KEY `uq_speech_analysis_speech_id` (`speech_id`),
    CONSTRAINT `fk_speech_analysis_speech`
        FOREIGN KEY (`speech_id`) REFERENCES `speeches` (`speech_id`)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='스피치 분석 결과 (Azure + LLM)';


-- ──────────────────────────────────────────────
-- 6. dashboard_stats
--    유저별 홈 화면 통계 캐시 (최근 5~10회 기준)
-- ──────────────────────────────────────────────
CREATE TABLE `dashboard_stats` (
    `stats_id`                  BIGINT      NOT NULL AUTO_INCREMENT,
    `user_id`                   BIGINT      NOT NULL,
    `average_score`             DOUBLE      NULL        COMMENT '전체 평균 점수',
    `today_practice_seconds`    INT         NULL        DEFAULT 0  COMMENT '오늘 누적 연습 시간(초)',
    `total_count`               INT         NULL        DEFAULT 0  COMMENT '누적 스피치 횟수',
    `summary_feedback`          TEXT        NULL        COMMENT 'GPT 종합 피드백 (최근 5~10회 기반)',
    `last_updated`              TIMESTAMP   NOT NULL    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`stats_id`),
    UNIQUE KEY `uq_dashboard_stats_user_id` (`user_id`),
    CONSTRAINT `fk_dashboard_stats_user`
        FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='홈 화면 통계 캐시';


-- ──────────────────────────────────────────────
-- 7. monthly_stats
--    월별 평균 점수 (홈 화면 "월간 평균 점수" 차트용)
-- ──────────────────────────────────────────────
CREATE TABLE `monthly_stats` (
    `monthly_stats_id`  BIGINT          NOT NULL AUTO_INCREMENT,
    `user_id`           BIGINT          NOT NULL,
    `year_month`        CHAR(7)         NOT NULL    COMMENT 'YYYY-MM 형식 예: 2025-04',
    `average_score`     DOUBLE          NULL,
    `practice_count`    INT             NULL        DEFAULT 0,
    `updated_at`        TIMESTAMP       NOT NULL    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`monthly_stats_id`),
    UNIQUE KEY `uq_monthly_stats_user_month` (`user_id`, `year_month`),
    CONSTRAINT `fk_monthly_stats_user`
        FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='월별 학습 통계';


-- ──────────────────────────────────────────────
-- 8. daily_words
--    데일리 단어 퀴즈 문제 풀 (학습 수준별)
-- ──────────────────────────────────────────────
CREATE TABLE `daily_words` (
    `word_id`       BIGINT          NOT NULL AUTO_INCREMENT,
    `word`          VARCHAR(100)    NOT NULL    COMMENT '퀴즈 정답 단어',
    `description`   TEXT            NOT NULL    COMMENT '단어 설명 (문제 지문)',
    `answer`        VARCHAR(100)    NOT NULL    COMMENT '정답 선택지',
    `option2`       VARCHAR(100)    NOT NULL    COMMENT '오답 선택지 1',
    `option3`       VARCHAR(100)    NOT NULL    COMMENT '오답 선택지 2',
    `target_level`  VARCHAR(20)     NOT NULL    COMMENT '대상 학습 수준',
    `created_at`    TIMESTAMP       NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`word_id`),
    KEY `idx_daily_words_level` (`target_level`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='데일리 단어 퀴즈 문제';


-- ──────────────────────────────────────────────
-- 9. daily_quiz_logs
--    유저별 퀴즈 응답 이력
-- ──────────────────────────────────────────────
CREATE TABLE `daily_quiz_logs` (
    `log_id`        BIGINT      NOT NULL AUTO_INCREMENT,
    `user_id`       BIGINT      NOT NULL,
    `word_id`       BIGINT      NOT NULL,
    `is_correct`    BOOLEAN     NOT NULL,
    `answered_at`   TIMESTAMP   NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`log_id`),
    KEY `idx_daily_quiz_logs_user_id` (`user_id`),
    KEY `idx_daily_quiz_logs_word_id` (`word_id`),
    CONSTRAINT `fk_daily_quiz_logs_user`
        FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
        ON DELETE CASCADE,
    CONSTRAINT `fk_daily_quiz_logs_word`
        FOREIGN KEY (`word_id`) REFERENCES `daily_words` (`word_id`)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='데일리 퀴즈 응답 이력';