package com.codit.talktalkcoach.repository;

import com.codit.talktalkcoach.domain.entity.DashboardStats;
import com.codit.talktalkcoach.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DashboardStatsRepository extends JpaRepository<DashboardStats, Long> {
    Optional<DashboardStats> findByUser(User user);
}
