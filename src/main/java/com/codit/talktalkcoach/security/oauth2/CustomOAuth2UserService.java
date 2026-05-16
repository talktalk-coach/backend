package com.codit.talktalkcoach.security.oauth2;

import com.codit.talktalkcoach.domain.entity.User;
import com.codit.talktalkcoach.domain.enums.Provider;
import com.codit.talktalkcoach.repository.UserRepository;
import com.codit.talktalkcoach.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId().toUpperCase();
        Provider provider = Provider.valueOf(registrationId);

        OAuth2UserInfo info = OAuth2UserInfo.of(provider, oAuth2User.getAttributes());

        User user = userRepository.findByEmail(info.getEmail())
                .orElseGet(() -> userRepository.save(
                        User.builder()
                                .email(info.getEmail())
                                .nickname(info.getNickname())
                                .profileImageUrl(info.getProfileImageUrl())
                                .provider(provider)
                                .build()
                ));

        return new CustomUserDetails(user);
    }
}
