package com.codit.talktalkcoach.service;

import com.codit.talktalkcoach.domain.entity.EmailVerification;
import com.codit.talktalkcoach.domain.entity.User;
import com.codit.talktalkcoach.domain.enums.VerificationType;
import com.codit.talktalkcoach.exception.ErrorCode;
import com.codit.talktalkcoach.exception.custom.EmailVerificationException;
import com.codit.talktalkcoach.repository.EmailVerificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final EmailVerificationRepository emailVerificationRepository;
    private final JavaMailSender mailSender;

    private static final int CODE_LENGTH = 6;
    private static final int EXPIRE_MINUTES = 10;

    @Transactional
    public void sendCode(String email, VerificationType type, User user) {
        String code = generateCode();

        EmailVerification verification = EmailVerification.builder()
                .user(user)
                .email(email)
                .type(type)
                .verificationCode(code)
                .expiredAt(LocalDateTime.now().plusMinutes(EXPIRE_MINUTES))
                .build();

        emailVerificationRepository.save(verification);
        sendEmail(email, code, type);
        log.info("인증 코드 발송 완료 - email: {}, type: {}", email, type);
    }

    @Transactional
    public void verify(String email, String code, VerificationType type) {
        EmailVerification verification = emailVerificationRepository
                .findTopByEmailAndTypeOrderByCreatedAtDesc(email, type)
                .orElseThrow(() -> new EmailVerificationException(ErrorCode.VERIFICATION_CODE_NOT_FOUND));

        if (verification.isExpired()) {
            throw new EmailVerificationException(ErrorCode.VERIFICATION_CODE_EXPIRED);
        }
        if (!verification.getVerificationCode().equals(code)) {
            throw new EmailVerificationException(ErrorCode.VERIFICATION_CODE_MISMATCH);
        }

        verification.verify();
    }

    public boolean isVerified(String email, VerificationType type) {
        return emailVerificationRepository
                .findTopByEmailAndTypeOrderByCreatedAtDesc(email, type)
                .map(EmailVerification::isVerified)
                .orElse(false);
    }

    private String generateCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    private void sendEmail(String to, String code, VerificationType type) {
        String subject = type == VerificationType.PARENT
                ? "[TalkTalk Coach] 보호자 이메일 인증 코드"
                : "[TalkTalk Coach] 이메일 인증 코드";
        String text = String.format("인증 코드: %s\n\n%d분 이내에 입력해주세요.", code, EXPIRE_MINUTES);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}
