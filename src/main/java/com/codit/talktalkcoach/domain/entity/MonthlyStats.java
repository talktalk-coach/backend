package com.codit.talktalkcoach.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "monthly_stats",
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "stat_month"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MonthlyStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long monthlyStatsId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // year_month → stat_month (year_month은 MySQL 8.0 예약어라 DDL 오류 발생)
    @Column(name = "stat_month", nullable = false, length = 7)
    private String yearMonth; // YYYY-MM

    private Double averageScore;

    private Integer practiceCount = 0;

    private LocalDateTime updatedAt = LocalDateTime.now();

    @Builder
    public MonthlyStats(User user, String yearMonth) {
        this.user = user;
        this.yearMonth = yearMonth;
    }

    public void update(Double averageScore, Integer practiceCount) {
        this.averageScore = averageScore;
        this.practiceCount = practiceCount;
        this.updatedAt = LocalDateTime.now();
    }
}
