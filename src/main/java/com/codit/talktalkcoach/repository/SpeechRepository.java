package com.codit.talktalkcoach.repository;

import com.codit.talktalkcoach.domain.entity.Speech;
import com.codit.talktalkcoach.domain.entity.User;
import com.codit.talktalkcoach.domain.enums.SpeechStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SpeechRepository extends JpaRepository<Speech, Long> {

    Page<Speech> findByUser(User user, Pageable pageable);

    // score 정렬용: speech_analysis의 평균 점수 JOIN 후 내림차순
    @Query("SELECT s FROM Speech s LEFT JOIN SpeechAnalysis a ON a.speech = s " +
           "WHERE s.user = :user " +
           "ORDER BY (COALESCE(a.accuracyScore,0) + COALESCE(a.fluencyScore,0) + " +
           "COALESCE(a.prosodyScore,0) + COALESCE(a.vocabularyScore,0) + " +
           "COALESCE(a.logicScore,0) + COALESCE(a.structureScore,0)) / 6.0 DESC")
    Page<Speech> findByUserOrderByAverageScoreDesc(
            @Param("user") User user, Pageable pageable);

    // score 정렬용: speech_analysis의 평균 점수 JOIN 후 오름차순
    @Query("SELECT s FROM Speech s LEFT JOIN SpeechAnalysis a ON a.speech = s " +
           "WHERE s.user = :user " +
           "ORDER BY (COALESCE(a.accuracyScore,0) + COALESCE(a.fluencyScore,0) + " +
           "COALESCE(a.prosodyScore,0) + COALESCE(a.vocabularyScore,0) + " +
           "COALESCE(a.logicScore,0) + COALESCE(a.structureScore,0)) / 6.0 ASC")
    Page<Speech> findByUserOrderByAverageScoreAsc(
            @Param("user") User user, Pageable pageable);

    List<Speech> findTop10ByUserOrderByCreatedAtDesc(User user);
    List<Speech> findTop3ByUserOrderByCreatedAtDesc(User user);

    // 성장치 히스토리용
    List<Speech> findByUserAndStatusOrderByCreatedAtAsc(User user, SpeechStatus status);

    // 특정 기간 + 상태 조회 (월간 통계, 오늘 연습시간 계산에 사용)
    @Query("SELECT s FROM Speech s WHERE s.user = :user AND s.status = :status " +
           "AND s.createdAt >= :from AND s.createdAt < :to")
    List<Speech> findByUserAndStatusAndCreatedAtBetween(
            @Param("user")   User user,
            @Param("status") SpeechStatus status,
            @Param("from")   LocalDateTime from,
            @Param("to")     LocalDateTime to);
}
