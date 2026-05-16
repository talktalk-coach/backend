package com.codit.talktalkcoach.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // 공통
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "잘못된 입력 값입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),

    // 인증
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 리프레시 토큰입니다."),

    // 회원
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),
    NICKNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 사용 중인 닉네임입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 올바르지 않습니다."),

    // 이메일 인증
    VERIFICATION_CODE_NOT_FOUND(HttpStatus.NOT_FOUND, "인증 코드를 찾을 수 없습니다."),
    VERIFICATION_CODE_EXPIRED(HttpStatus.BAD_REQUEST, "인증 코드가 만료되었습니다."),
    VERIFICATION_CODE_MISMATCH(HttpStatus.BAD_REQUEST, "인증 코드가 일치하지 않습니다."),
    PARENT_EMAIL_NOT_VERIFIED(HttpStatus.FORBIDDEN, "보호자 이메일 인증이 필요합니다."),

    // 스피치
    SPEECH_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 스피치입니다."),
    SPEECH_ANALYSIS_NOT_FOUND(HttpStatus.NOT_FOUND, "분석 결과를 찾을 수 없습니다."),
    SPEECH_STILL_PROCESSING(HttpStatus.ACCEPTED, "스피치 분석이 아직 진행 중입니다."),
    SPEECH_ANALYSIS_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "스피치 분석에 실패했습니다."),

    // 외부 API
    AZURE_API_ERROR(HttpStatus.BAD_GATEWAY, "Azure Speech API 오류가 발생했습니다."),
    GPT_API_ERROR(HttpStatus.BAD_GATEWAY, "GPT API 오류가 발생했습니다."),

    // 퀴즈
    QUIZ_WORD_NOT_FOUND(HttpStatus.NOT_FOUND, "오늘의 퀴즈 단어가 없습니다."),
    QUIZ_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 퀴즈입니다."),
    QUIZ_ALREADY_ANSWERED(HttpStatus.CONFLICT, "오늘 이미 제출한 퀴즈입니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
