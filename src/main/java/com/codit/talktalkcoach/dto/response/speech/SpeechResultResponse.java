package com.codit.talktalkcoach.dto.response.speech;

import com.codit.talktalkcoach.domain.entity.Speech;
import com.codit.talktalkcoach.domain.entity.SpeechAnalysis;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SpeechResultResponse {

    private Long speechId;
    private String title;
    private int duration;
    private LocalDateTime createdAt;

    private Double averageScore;

    // Azure 항목 (completenessScore 제거)
    private Double accuracyScore;
    private Double fluencyScore;
    private Double prosodyScore;

    // LLM 항목 (sentenceScore 제거)
    private Double vocabularyScore;
    private Double logicScore;
    private Double structureScore;

    private Integer wordCount;

    private String vocabularyFeedback;
    private String sentenceStructureFeedback;
    private String logicFeedback;
    private String overallFeedback;
    private String customPlan;
    private String transcript;

    // 직전 5개 스피치 평균 대비 성장 멘트
    private String progress;

    public static SpeechResultResponse of(Speech speech, SpeechAnalysis analysis, String progress) {
        return SpeechResultResponse.builder()
                .speechId(speech.getSpeechId())
                .title(speech.getTitle())
                .duration(speech.getDuration())
                .createdAt(speech.getCreatedAt())
                .averageScore(analysis.calculateAverageScore())
                .accuracyScore(analysis.getAccuracyScore())
                .fluencyScore(analysis.getFluencyScore())
                .prosodyScore(analysis.getProsodyScore())
                .vocabularyScore(analysis.getVocabularyScore())
                .logicScore(analysis.getLogicScore())
                .structureScore(analysis.getStructureScore())
                .wordCount(analysis.getWordCount())
                .vocabularyFeedback(analysis.getVocabularyFeedback())
                .sentenceStructureFeedback(analysis.getSentenceStructureFeedback())
                .logicFeedback(analysis.getLogicFeedback())
                .overallFeedback(analysis.getOverallFeedback())
                .customPlan(analysis.getCustomPlan())
                .transcript(analysis.getTranscript())
                .progress(progress)
                .build();
    }
}
