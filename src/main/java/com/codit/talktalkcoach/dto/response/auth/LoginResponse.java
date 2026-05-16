package com.codit.talktalkcoach.dto.response.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private boolean isNewUser;      // true → 학습 수준 선택 화면으로 이동
    private String nickname;
    private String profileImageUrl;
}
