package com.codit.talktalkcoach.dto.request.speech;

import com.codit.talktalkcoach.domain.enums.TargetLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GptFeedbackTestRequest {

    private String context;

    private TargetLevel targetLevel = TargetLevel.MIDDLE_1_2;
}
