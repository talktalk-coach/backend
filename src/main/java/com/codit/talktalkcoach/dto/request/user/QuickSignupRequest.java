package com.codit.talktalkcoach.dto.request.user;

import com.codit.talktalkcoach.domain.enums.TargetLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class QuickSignupRequest {

    private String email     = "test@test.com";
    private String password  = "Test1234!";
    private String nickname  = "테스터";
    private TargetLevel targetLevel = TargetLevel.MIDDLE_1_2;
}
