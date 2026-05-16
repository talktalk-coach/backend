package com.codit.talktalkcoach.security.oauth2;

import com.codit.talktalkcoach.domain.entity.RefreshToken;
import com.codit.talktalkcoach.domain.entity.User;
import com.codit.talktalkcoach.repository.RefreshTokenRepository;
import com.codit.talktalkcoach.security.CustomUserDetails;
import com.codit.talktalkcoach.security.jwt.JwtProvider;
import com.codit.talktalkcoach.security.jwt.JwtProperties;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final JwtProperties jwtProperties;
    private final RefreshTokenRepository refreshTokenRepository;

    // 프론트엔드 리다이렉트 URL (application.yml에서 주입 가능하도록 분리 권장)
    private static final String REDIRECT_URI = "http://localhost:3000/oauth2/callback";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        String accessToken  = jwtProvider.generateAccessToken(user.getUserId(), user.getEmail());
        String refreshToken = jwtProvider.generateRefreshToken(user.getUserId());

        // RefreshToken 저장 (기존 토큰이 있으면 rotate)
        refreshTokenRepository.findByUser(user).ifPresentOrElse(
                rt -> rt.rotate(refreshToken,
                        LocalDateTime.now().plusSeconds(jwtProperties.getRefreshTokenExpiry() / 1000)),
                () -> refreshTokenRepository.save(RefreshToken.builder()
                        .user(user)
                        .tokenValue(refreshToken)
                        .expiredAt(LocalDateTime.now().plusSeconds(jwtProperties.getRefreshTokenExpiry() / 1000))
                        .build())
        );

        String redirectUrl = REDIRECT_URI
                + "?accessToken=" + accessToken
                + "&refreshToken=" + refreshToken
                + "&isNewUser=" + (user.getTargetLevel() == null);

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}
