package com.codit.talktalkcoach.repository;

import com.codit.talktalkcoach.domain.entity.DailyWord;
import com.codit.talktalkcoach.domain.enums.TargetLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface DailyWordRepository extends JpaRepository<DailyWord, Long> {

    // 오늘 특정 레벨용으로 생성된 퀴즈 3개 조회
    @Query("SELECT d FROM DailyWord d WHERE d.targetLevel = :level " +
           "AND d.createdAt >= :from AND d.createdAt < :to ORDER BY d.createdAt ASC")
    List<DailyWord> findTodayQuizByLevel(
            @Param("level") TargetLevel level,
            @Param("from")  LocalDateTime from,
            @Param("to")    LocalDateTime to);

    // 오늘 이전 퀴즈 전체 삭제 (매일 갱신 시 누적 방지)
    @Modifying
    @Query("DELETE FROM DailyWord d WHERE d.createdAt < :todayStart")
    void deleteOldQuiz(@Param("todayStart") LocalDateTime todayStart);

    // 오늘 생성된 퀴즈 삭제 (재생성용)
    @Modifying
    @Query("DELETE FROM DailyWord d WHERE d.createdAt >= :from AND d.createdAt < :to")
    void deleteTodayQuiz(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    long countByTargetLevel(TargetLevel targetLevel);
}
