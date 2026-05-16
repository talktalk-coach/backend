package com.codit.talktalkcoach.repository;

import com.codit.talktalkcoach.domain.entity.EmailVerification;
import com.codit.talktalkcoach.domain.enums.VerificationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {
    Optional<EmailVerification> findTopByEmailAndTypeOrderByCreatedAtDesc(
            String email, VerificationType type);
}
