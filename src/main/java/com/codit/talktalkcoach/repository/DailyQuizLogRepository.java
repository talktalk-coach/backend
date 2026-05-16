package com.codit.talktalkcoach.repository;

import com.codit.talktalkcoach.domain.entity.DailyQuizLog;
import com.codit.talktalkcoach.domain.entity.DailyWord;
import com.codit.talktalkcoach.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DailyQuizLogRepository extends JpaRepository<DailyQuizLog, Long> {

    // 오늘 정답 개수
    @Query("SELECT COUNT(d) FROM DailyQuizLog d WHERE d.user = :user " +
           "AND d.answeredAt >= :from AND d.answeredAt < :to AND d.isCorrect = true")
    long countTodayCorrect(@Param("user") User user,
                           @Param("from") LocalDateTime from,
                           @Param("to")   LocalDateTime to);

    // 오늘 특정 유저의 퀴즈 로그 전체
    List<DailyQuizLog> findByUserAndAnsweredAtBetween(
            User user, LocalDateTime from, LocalDateTime to);

    // 오늘 특정 퀴즈 제출 여부
    Optional<DailyQuizLog> findByUserAndDailyWordAndAnsweredAtBetween(
            User user, DailyWord dailyWord, LocalDateTime from, LocalDateTime to);

    // 오늘 시도 횟수 (기존 메서드 유지)
    @Query("SELECT COUNT(d) FROM DailyQuizLog d WHERE d.user = :user " +
           "AND DATE(d.answeredAt) = CURRENT_DATE")
    long countTodayAttempts(@Param("user") User user);
}
