package com.codit.talktalkcoach.dto.request.user;

import com.codit.talktalkcoach.domain.enums.TargetLevel;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GradeUpdateRequest {

    @NotNull(message = "학습 수준을 선택해주세요.")
    private TargetLevel targetLevel;
}
