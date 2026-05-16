package com.codit.talktalkcoach.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "daily_quiz_logs")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailyQuizLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "word_id", nullable = false)
    private DailyWord dailyWord;

    @Column(nullable = false)
    private boolean isCorrect;

    @Column(nullable = false, updatable = false)
    private LocalDateTime answeredAt = LocalDateTime.now();

    @Builder
    public DailyQuizLog(User user, DailyWord dailyWord, boolean isCorrect) {
        this.user = user;
        this.dailyWord = dailyWord;
        this.isCorrect = isCorrect;
    }
}
