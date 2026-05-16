package com.codit.talktalkcoach.dto.response.user;

import com.codit.talktalkcoach.domain.entity.User;
import com.codit.talktalkcoach.domain.enums.Provider;
import com.codit.talktalkcoach.domain.enums.TargetLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInfoResponse {
    private Long userId;
    private String email;
    private String nickname;
    private String profileImageUrl;
    private Provider provider;
    private TargetLevel targetLevel;
    private boolean isUnder14;

    public static UserInfoResponse from(User user) {
        return UserInfoResponse.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImageUrl(user.getProfileImageUrl())
                .provider(user.getProvider())
                .targetLevel(user.getTargetLevel())
                .isUnder14(user.isUnder14())
                .build();
    }
}
