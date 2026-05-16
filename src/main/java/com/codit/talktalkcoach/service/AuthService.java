package com.codit.talktalkcoach.service;

import com.codit.talktalkcoach.domain.entity.RefreshToken;
import com.codit.talktalkcoach.domain.entity.User;
import com.codit.talktalkcoach.domain.enums.VerificationType;
import com.codit.talktalkcoach.dto.request.auth.LoginRequest;
import com.codit.talktalkcoach.dto.request.auth.SignupRequest;
import com.codit.talktalkcoach.dto.request.auth.TokenRefreshRequest;
import com.codit.talktalkcoach.dto.response.auth.LoginResponse;
import com.codit.talktalkcoach.dto.response.auth.TokenResponse;
import com.codit.talktalkcoach.exception.BusinessException;
import com.codit.talktalkcoach.exception.ErrorCode;
import com.codit.talktalkcoach.exception.custom.UserNotFoundException;
import com.codit.talktalkcoach.repository.RefreshTokenRepository;
import com.codit.talktalkcoach.repository.UserRepository;
import com.codit.talktalkcoach.security.jwt.JwtProperties;
import com.codit.talktalkcoach.security.jwt.JwtProvider;
import com.codit.talktalkcoach.util.AgeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final JwtProperties jwtProperties;
    private final EmailVerificationService emailVerificationService;

    @Transactional
    public void signup(SignupRequest request) {
        // 이메일 중복 체크
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        // 닉네임 중복 체크
        if (userRepository.existsByNickname(request.getNickname())) {
            throw new BusinessException(ErrorCode.NICKNAME_ALREADY_EXISTS);
        }

        boolean isUnder14 = request.getBirthDate() != null
                && AgeUtil.isUnder14(request.getBirthDate());

        // 14세 미만이면 보호자 이메일 인증 확인
        if (isUnder14) {
            if (request.getParentEmail() == null || request.getParentEmail().isBlank()) {
                throw new BusinessException(ErrorCode.PARENT_EMAIL_NOT_VERIFIED);
            }
            if (!emailVerificationService.isVerified(request.getParentEmail(), VerificationType.PARENT)) {
                throw new BusinessException(ErrorCode.PARENT_EMAIL_NOT_VERIFIED);
            }
        }

        // 본인 이메일 인증 확인
        if (!emailVerificationService.isVerified(request.getEmail(), VerificationType.SELF)) {
            throw new BusinessException(ErrorCode.VERIFICATION_CODE_NOT_FOUND);
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .birthDate(request.getBirthDate())
                .isUnder14(isUnder14)
                .parentEmail(request.getParentEmail())
                .build();

        userRepository.save(user);
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_PASSWORD);
        }

        return issueTokens(user);
    }

    @Transactional
    public TokenResponse refresh(TokenRefreshRequest request) {
        RefreshToken saved = refreshTokenRepository.findByTokenValue(request.getRefreshToken())
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN));

        if (saved.isExpired()) {
            refreshTokenRepository.delete(saved);
            throw new BusinessException(ErrorCode.EXPIRED_TOKEN);
        }

        User user = saved.getUser();
        String newAccess  = jwtProvider.generateAccessToken(user.getUserId(), user.getEmail());
        String newRefresh = jwtProvider.generateRefreshToken(user.getUserId());

        saved.rotate(newRefresh,
                LocalDateTime.now().plusSeconds(jwtProperties.getRefreshTokenExpiry() / 1000));

        return TokenResponse.builder()
                .accessToken(newAccess)
                .refreshToken(newRefresh)
                .build();
    }

    @Transactional
    public void logout(User user) {
        refreshTokenRepository.findByUser(user)
                .ifPresent(refreshTokenRepository::delete);
    }

    // ─── internal ───────────────────────────────────────────────────────────────
    private LoginResponse issueTokens(User user) {
        String accessToken  = jwtProvider.generateAccessToken(user.getUserId(), user.getEmail());
        String refreshToken = jwtProvider.generateRefreshToken(user.getUserId());

        LocalDateTime expiredAt = LocalDateTime.now()
                .plusSeconds(jwtProperties.getRefreshTokenExpiry() / 1000);

        refreshTokenRepository.findByUser(user).ifPresentOrElse(
                rt -> rt.rotate(refreshToken, expiredAt),
                () -> refreshTokenRepository.save(RefreshToken.builder()
                        .user(user).tokenValue(refreshToken).expiredAt(expiredAt).build())
        );

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .isNewUser(user.getTargetLevel() == null)
                .nickname(user.getNickname())
                .profileImageUrl(user.getProfileImageUrl())
                .build();
    }
}
