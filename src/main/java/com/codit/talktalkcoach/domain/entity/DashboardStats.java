package com.codit.talktalkcoach.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "dashboard_stats")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DashboardStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long statsId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    private Double averageScore;

    private Integer todayPracticeSeconds = 0;

    private Integer totalCount = 0;

    // @Lob 제거 → TEXT와 일치
    @Column(columnDefinition = "TEXT")
    private String summaryFeedback;

    private LocalDateTime lastUpdated = LocalDateTime.now();

    @Builder
    public DashboardStats(User user) {
        this.user = user;
    }

    public void updateFeedback(String summaryFeedbackJson) {
        this.summaryFeedback = summaryFeedbackJson;  // JSON 배열 문자열로 저장
        this.lastUpdated = LocalDateTime.now();
    }

    public void update(Double averageScore, Integer todayPracticeSeconds,
                       Integer totalCount, String summaryFeedback) {
        this.averageScore = averageScore;
        this.todayPracticeSeconds = todayPracticeSeconds;
        this.totalCount = totalCount;
        this.summaryFeedback = summaryFeedback;
        this.lastUpdated = LocalDateTime.now();
    }

    public void addPracticeSeconds(int seconds) {
        this.todayPracticeSeconds =
                (this.todayPracticeSeconds == null ? 0 : this.todayPracticeSeconds) + seconds;
    }

    public void incrementTotalCount() {
        this.totalCount = (this.totalCount == null ? 0 : this.totalCount) + 1;
    }
}
