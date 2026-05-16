package com.codit.talktalkcoach.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "speech_analysis")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SpeechAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long analysisId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "speech_id", nullable = false, unique = true)
    private Speech speech;

    @Column(columnDefinition = "TEXT")
    private String transcript;

    // Azure 평가 항목 (completenessScore 제거)
    private Double accuracyScore;
    private Double fluencyScore;
    private Double prosodyScore;

    // LLM 평가 항목 (sentenceScore 제거)
    private Double vocabularyScore;
    private Integer wordCount;
    private Double logicScore;
    private Double structureScore;

    // LLM 피드백 텍스트
    @Column(columnDefinition = "TEXT") private String vocabularyFeedback;
    @Column(columnDefinition = "TEXT") private String sentenceStructureFeedback;
    @Column(columnDefinition = "TEXT") private String logicFeedback;
    @Column(columnDefinition = "TEXT") private String overallFeedback;
    @Column(columnDefinition = "TEXT") private String customPlan;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "JSON")
    private Map<String, Object> wordLevelData;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder
    public SpeechAnalysis(Speech speech, String transcript,
                          Double accuracyScore, Double fluencyScore, Double prosodyScore,
                          Double vocabularyScore, Integer wordCount,
                          Double logicScore, Double structureScore,
                          String vocabularyFeedback, String sentenceStructureFeedback,
                          String logicFeedback, String overallFeedback, String customPlan,
                          Map<String, Object> wordLevelData) {
        this.speech = speech;
        this.transcript = transcript;
        this.accuracyScore = accuracyScore;
        this.fluencyScore = fluencyScore;
        this.prosodyScore = prosodyScore;
        this.vocabularyScore = vocabularyScore;
        this.wordCount = wordCount;
        this.logicScore = logicScore;
        this.structureScore = structureScore;
        this.vocabularyFeedback = vocabularyFeedback;
        this.sentenceStructureFeedback = sentenceStructureFeedback;
        this.logicFeedback = logicFeedback;
        this.overallFeedback = overallFeedback;
        this.customPlan = customPlan;
        this.wordLevelData = wordLevelData;
    }

    // 평균 계산: accuracy, fluency, prosody, vocabulary, logic, structure (6개)
    public double calculateAverageScore() {
        double sum = 0;
        int count = 0;
        Double[] scores = {accuracyScore, fluencyScore, prosodyScore,
                           vocabularyScore, logicScore, structureScore};
        for (Double s : scores) {
            if (s != null) { sum += s; count++; }
        }
        return count == 0 ? 0 : sum / count;
    }
}
