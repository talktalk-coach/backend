package com.codit.talktalkcoach.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class SignupRequest {

    @NotBlank @Email
    private String email;

    @NotBlank @Size(min = 8, max = 20)
    private String password;

    @NotBlank @Size(min = 2, max = 20, message = "닉네임은 2자 이상 20자 이하로 입력해주세요.")
    private String nickname;

    private LocalDate birthDate;      // 만 14세 미만 판별용

    private String parentEmail;       // 만 14세 미만인 경우 필수
}
