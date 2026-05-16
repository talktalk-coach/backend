package com.codit.talktalkcoach.dto.request.speech;

import com.codit.talktalkcoach.domain.enums.SpeechCategory;
import com.codit.talktalkcoach.domain.enums.TargetLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GptTestRequest {

    private String transcript;

    private TargetLevel targetLevel = TargetLevel.MIDDLE_1_2;

    private SpeechCategory category = SpeechCategory.PRESENTATION;
}
