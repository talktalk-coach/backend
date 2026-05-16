package com.codit.talktalkcoach.repository;

import com.codit.talktalkcoach.domain.entity.MonthlyStats;
import com.codit.talktalkcoach.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MonthlyStatsRepository extends JpaRepository<MonthlyStats, Long> {
    Optional<MonthlyStats> findByUserAndYearMonth(User user, String yearMonth);
    List<MonthlyStats> findByUserOrderByYearMonthDesc(User user);
}
