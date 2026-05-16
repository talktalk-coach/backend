package com.codit.talktalkcoach.external.gpt.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CustomPlanItem {
    private String title;
    private String description;
    private String category; // VOCABULARY | SENTENCE | LOGIC | STRUCTURE | PACING | FILLER
}
