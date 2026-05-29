package com.codit.talktalkcoach.domain.entity;

import com.codit.talktalkcoach.domain.enums.SpeechCategory;
import com.codit.talktalkcoach.domain.enums.SpeechStatus;
import com.codit.talktalkcoach.domain.enums.TargetLevel;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Table(name = "speeches")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE speeches SET deleted_at = NOW() WHERE speech_id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Speech {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long speechId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 최초 사용자 입력값으로 저장되며, 분석 완료 후 GPT가 생성한 주제로 덮어씀
    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 512)
    private String audioUrl;

    @Column(nullable = false)
    private int duration; // 초 단위

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TargetLevel targetLevel;

    // 스피치 유형 (PRESENTATION | SPEECH | DEBATE)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SpeechCategory category = SpeechCategory.PRESENTATION;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SpeechStatus status = SpeechStatus.PROCESSING;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime deletedAt;

    @Builder
    public Speech(User user, String title, String audioUrl, int duration,
                  TargetLevel targetLevel, SpeechCategory category) {
        this.user = user;
        this.title = title;
        this.audioUrl = audioUrl;
        this.duration = duration;
        this.targetLevel = targetLevel;
        this.category = category != null ? category : SpeechCategory.PRESENTATION;
    }

    public void complete() { this.status = SpeechStatus.COMPLETED; }
    public void fail()     { this.status = SpeechStatus.FAILED; }
    // GPT 분석 완료 후 주제로 title 갱신
    public void updateTitle(String title) { this.title = title; }
}
