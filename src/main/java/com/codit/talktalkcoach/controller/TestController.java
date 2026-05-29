package com.codit.talktalkcoach.controller;

import com.codit.talktalkcoach.domain.entity.RefreshToken;
import com.codit.talktalkcoach.domain.entity.User;
import com.codit.talktalkcoach.domain.enums.TargetLevel;
import com.codit.talktalkcoach.dto.request.speech.GptFeedbackTestRequest;
import com.codit.talktalkcoach.dto.request.speech.GptTestRequest;
import com.codit.talktalkcoach.dto.request.user.QuickSignupRequest;
import com.codit.talktalkcoach.dto.response.auth.LoginResponse;
import com.codit.talktalkcoach.external.gpt.GptClient;
import com.codit.talktalkcoach.external.gpt.dto.GptAnalysisResult;
import com.codit.talktalkcoach.repository.RefreshTokenRepository;
import com.codit.talktalkcoach.repository.UserRepository;
import com.codit.talktalkcoach.security.jwt.JwtProperties;
import com.codit.talktalkcoach.security.jwt.JwtProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Tag(name = "테스트 (개발 전용)", description = "local 프로파일에서만 사용 가능한 테스트 API")
@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
// [임시] 배포 환경 테스트용으로 prod, default 프로파일 추가
// TODO: 테스트 완료 후 @Profile("local") 으로 되돌릴 것
@Profile({"local", "prod", "default"})
public class TestController {

    private final GptClient gptClient;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final JwtProperties jwtProperties;

    // ─── 즉시 회원가입 + 로그인 ──────────────────────────────────────────
    @Operation(
            summary = "[테스트] 즉시 회원가입 + 로그인",
            description = """
                    이메일 인증 없이 바로 회원가입 후 토큰을 발급합니다. local 전용.
                    
                    JSON Body 예시:
                    {
                      "email": "test@test.com",
                      "password": "Test1234!",
                      "nickname": "테스터",
                      "targetLevel": "ELEM_3_4"
                    }
                    """
    )
    @PostMapping("/quick-signup")
    public ResponseEntity<LoginResponse> quickSignup(
            @RequestBody QuickSignupRequest request) {

        User user = userRepository.findByEmail(request.getEmail()).orElseGet(() -> {
            User newUser = User.builder()
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .nickname(request.getNickname())
                    .targetLevel(request.getTargetLevel())
                    .build();
            return userRepository.save(newUser);
        });

        if (user.getTargetLevel() == null) {
            user.updateTargetLevel(request.getTargetLevel());
        }

        String accessToken  = jwtProvider.generateAccessToken(user.getUserId(), user.getEmail());
        String refreshToken = jwtProvider.generateRefreshToken(user.getUserId());
        LocalDateTime expiredAt = LocalDateTime.now()
                .plusSeconds(jwtProperties.getRefreshTokenExpiry() / 1000);

        refreshTokenRepository.findByUser(user).ifPresentOrElse(
                rt -> rt.rotate(refreshToken, expiredAt),
                () -> refreshTokenRepository.save(RefreshToken.builder()
                        .user(user).tokenValue(refreshToken).expiredAt(expiredAt).build())
        );

        return ResponseEntity.ok(LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .isNewUser(false)
                .nickname(user.getNickname())
                .profileImageUrl(user.getProfileImageUrl())
                .build());
    }

    // ─── GPT 분석 단독 테스트 ─────────────────────────────────────────────
    @Operation(
            summary = "[테스트] GPT 분석 단독 테스트",
            description = """
                    텍스트를 직접 입력하여 GPT 스피치 분석 결과를 확인합니다.
                    Azure STT 없이 GPT 단독으로 동작합니다.
                    
                    **targetLevel**: ELEM_1_2 | ELEM_3_4 | ELEM_5_6 | MIDDLE_1_2 | MIDDLE_3
                    **category**: PRESENTATION | SPEECH | DEBATE (초1~4는 PRESENTATION 자동 보정)
                    
                    JSON Body 예시:
                    {
                      "transcript": "학교 수업이 끝나면 나는 도서관으로 향합니다.",
                      "targetLevel": "ELEM_3_4",
                      "category": "PRESENTATION"
                    }
                    """
    )
    @PostMapping("/gpt-analyze")
    public ResponseEntity<GptAnalysisResult> testGptAnalyze(
            @RequestBody GptTestRequest request) {

        GptAnalysisResult result = gptClient.analyzeSpeech(
                request.getTranscript(),
                request.getTargetLevel(),
                request.getCategory());
        return ResponseEntity.ok(result);
    }

    // ─── GPT 종합 피드백 테스트 ───────────────────────────────────────────
    @Operation(
            summary = "[테스트] GPT 종합 피드백 테스트",
            description = """
                    최근 스피치 요약 텍스트를 입력하면 홈 화면용 GPT 피드백을 반환합니다.
                    
                    JSON Body 예시:
                    {
                      "context": "최근 5회 평균 점수: 72점. 정확도는 높지만 논리 구성이 약함.",
                      "targetLevel": "ELEM_3_4"
                    }
                    """
    )
    @PostMapping("/gpt-feedback")
    public ResponseEntity<java.util.List<String>> testGptFeedback(
            @RequestBody GptFeedbackTestRequest request) {

        java.util.List<String> feedback = gptClient.generateSummaryFeedback(
                request.getContext(),
                request.getTargetLevel());
        return ResponseEntity.ok(feedback);
    }
}
