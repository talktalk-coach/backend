package com.codit.talktalkcoach.domain.entity;

import com.codit.talktalkcoach.domain.enums.TargetLevel;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "daily_words")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailyWord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wordId;

    @Column(nullable = false, length = 100)
    private String word;

    // @Lob 제거 → TEXT와 일치
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, length = 100)
    private String answer;

    @Column(nullable = false, length = 100)
    private String option2;

    @Column(nullable = false, length = 100)
    private String option3;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TargetLevel targetLevel;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder
    public DailyWord(String word, String description, String answer,
                     String option2, String option3, TargetLevel targetLevel) {
        this.word = word;
        this.description = description;
        this.answer = answer;
        this.option2 = option2;
        this.option3 = option3;
        this.targetLevel = targetLevel;
    }
}
