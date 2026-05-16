package com.codit.talktalkcoach.domain.entity;

import com.codit.talktalkcoach.domain.enums.Provider;
import com.codit.talktalkcoach.domain.enums.TargetLevel;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE users SET deleted_at = NOW() WHERE user_id = ?")
@SQLRestriction("deleted_at IS NULL")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(length = 255)
    private String password;

    @Column(nullable = false, length = 50)
    private String nickname;

    @Column(length = 512)
    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Provider provider = Provider.LOCAL;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private TargetLevel targetLevel;

    private LocalDate birthDate;

    // SQL: is_under14 BOOLEAN NOT NULL DEFAULT FALSE
    @Column(nullable = false)
    private boolean isUnder14 = false;

    @Column(length = 100)
    private String parentEmail;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // SQL: updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE
    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    private LocalDateTime deletedAt;

    @Builder
    public User(String email, String password, String nickname, String profileImageUrl,
                Provider provider, TargetLevel targetLevel, LocalDate birthDate,
                boolean isUnder14, String parentEmail) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.provider = provider != null ? provider : Provider.LOCAL;
        this.targetLevel = targetLevel;
        this.birthDate = birthDate;
        this.isUnder14 = isUnder14;
        this.parentEmail = parentEmail;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateProfileImage(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void updateTargetLevel(TargetLevel targetLevel) {
        this.targetLevel = targetLevel;
    }

    public void updatePassword(String encodedPassword) {
        this.password = encodedPassword;
    }
}
