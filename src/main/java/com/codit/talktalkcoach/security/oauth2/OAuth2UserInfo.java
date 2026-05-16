package com.codit.talktalkcoach.security.oauth2;

import com.codit.talktalkcoach.domain.enums.Provider;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class OAuth2UserInfo {

    private final String email;
    private final String nickname;
    private final String profileImageUrl;
    private final Provider provider;

    public static OAuth2UserInfo of(Provider provider, Map<String, Object> attributes) {
        return switch (provider) {
            case GOOGLE -> ofGoogle(attributes);
            case KAKAO  -> ofKakao(attributes);
            default -> throw new IllegalArgumentException("지원하지 않는 소셜 로그인 provider: " + provider);
        };
    }

    @SuppressWarnings("unchecked")
    private static OAuth2UserInfo ofGoogle(Map<String, Object> attr) {
        return OAuth2UserInfo.builder()
                .email((String) attr.get("email"))
                .nickname((String) attr.get("name"))
                .profileImageUrl((String) attr.get("picture"))
                .provider(Provider.GOOGLE)
                .build();
    }

    @SuppressWarnings("unchecked")
    private static OAuth2UserInfo ofKakao(Map<String, Object> attr) {
        Map<String, Object> account  = (Map<String, Object>) attr.get("kakao_account");
        Map<String, Object> profile  = (Map<String, Object>) account.get("profile");
        return OAuth2UserInfo.builder()
                .email((String) account.get("email"))
                .nickname((String) profile.get("nickname"))
                .profileImageUrl((String) profile.get("profile_image_url"))
                .provider(Provider.KAKAO)
                .build();
    }
}
