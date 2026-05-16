package com.codit.talktalkcoach.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_tokens")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tokenId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 512)
    private String tokenValue;

    @Column(nullable = false)
    private LocalDateTime expiredAt;

    @Builder
    public RefreshToken(User user, String tokenValue, LocalDateTime expiredAt) {
        this.user = user;
        this.tokenValue = tokenValue;
        this.expiredAt = expiredAt;
    }

    public void rotate(String newTokenValue, LocalDateTime newExpiredAt) {
        this.tokenValue = newTokenValue;
        this.expiredAt = newExpiredAt;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiredAt);
    }
}
