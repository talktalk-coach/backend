package com.codit.talktalkcoach.controller;

import com.codit.talktalkcoach.domain.entity.User;
import com.codit.talktalkcoach.domain.enums.VerificationType;
import com.codit.talktalkcoach.dto.request.auth.*;
import com.codit.talktalkcoach.dto.response.auth.LoginResponse;
import com.codit.talktalkcoach.dto.response.auth.TokenResponse;
import com.codit.talktalkcoach.security.CustomUserDetails;
import com.codit.talktalkcoach.service.AuthService;
import com.codit.talktalkcoach.service.EmailVerificationService;
import com.codit.talktalkcoach.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final EmailVerificationService emailVerificationService;
    private final UserService userService;

    // 회원가입'
    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody SignupRequest request) {
        authService.signup(request);
        return ResponseEntity.ok().build();
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    // 본인 이메일 인증 코드 발송
    @PostMapping("/email/send")
    public ResponseEntity<Void> sendEmail(@Valid @RequestBody EmailSendRequest request) {
        emailVerificationService.sendCode(request.getEmail(), VerificationType.SELF, null);
        return ResponseEntity.ok().build();
    }

    // 본인 이메일 인증 코드 검증
    @PostMapping("/email/verify")
    public ResponseEntity<Void> verifyEmail(@Valid @RequestBody EmailVerifyRequest request) {
        emailVerificationService.verify(request.getEmail(), request.getCode(), VerificationType.SELF);
        return ResponseEntity.ok().build();
    }

    // 보호자 이메일 인증 코드 발송 (만 14세 미만)
    @PostMapping("/email/parent/send")
    public ResponseEntity<Void> sendParentEmail(@Valid @RequestBody EmailSendRequest request) {
        emailVerificationService.sendCode(request.getEmail(), VerificationType.PARENT, null);
        return ResponseEntity.ok().build();
    }

    // 보호자 이메일 인증 코드 검증
    @PostMapping("/email/parent/verify")
    public ResponseEntity<Void> verifyParentEmail(@Valid @RequestBody EmailVerifyRequest request) {
        emailVerificationService.verify(request.getEmail(), request.getCode(), VerificationType.PARENT);
        return ResponseEntity.ok().build();
    }

    // 토큰 재발급
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@Valid @RequestBody TokenRefreshRequest request) {
        return ResponseEntity.ok(authService.refresh(request));
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal CustomUserDetails userDetails) {
        authService.logout(userDetails.getUser());
        return ResponseEntity.ok().build();
    }
}
