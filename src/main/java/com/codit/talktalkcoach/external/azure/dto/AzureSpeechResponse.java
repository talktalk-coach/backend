package com.codit.talktalkcoach.external.azure.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@Builder
public class AzureSpeechResponse {

    private String transcript;

    // Azure 발음 평가 항목 (completenessScore 제거)
    private Double accuracyScore;
    private Double fluencyScore;
    private Double prosodyScore;

    private List<Map<String, Object>> words;
}
