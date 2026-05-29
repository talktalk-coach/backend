package com.codit.talktalkcoach.dto.response.user;

import com.codit.talktalkcoach.domain.entity.Speech;
import com.codit.talktalkcoach.domain.enums.SpeechStatus;
import com.codit.talktalkcoach.domain.enums.TargetLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class SpeechListResponse {

    private List<SpeechSummaryDto> speeches;
    private int totalCount;
    private int currentPage;
    private int totalPages;

    @Getter
    @Builder
    public static class SpeechSummaryDto {
        private Long speechId;
        private String title;
        private Double averageScore;
        private int duration;
        private TargetLevel targetLevel;
        private SpeechStatus status;
        private LocalDateTime createdAt;

        public static SpeechSummaryDto from(Speech speech, Double averageScore) {
            return SpeechSummaryDto.builder()
                    .speechId(speech.getSpeechId())
                    .title(speech.getTitle())
                    .averageScore(averageScore)
                    .duration(speech.getDuration())
                    .targetLevel(speech.getTargetLevel())
                    .status(speech.getStatus())
                    .createdAt(speech.getCreatedAt())
                    .build();
        }
    }
}
