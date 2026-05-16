package com.codit.talktalkcoach.domain.entity;

import com.codit.talktalkcoach.domain.enums.VerificationType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "email_verifications")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long verificationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, length = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private VerificationType type;

    @Column(nullable = false, length = 10)
    private String verificationCode;

    @Column(nullable = false)
    private boolean isVerified = false;

    @Column(nullable = false)
    private LocalDateTime expiredAt;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder
    public EmailVerification(User user, String email, VerificationType type,
                             String verificationCode, LocalDateTime expiredAt) {
        this.user = user;
        this.email = email;
        this.type = type;
        this.verificationCode = verificationCode;
        this.expiredAt = expiredAt;
    }

    public void verify() {
        this.isVerified = true;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiredAt);
    }
}
