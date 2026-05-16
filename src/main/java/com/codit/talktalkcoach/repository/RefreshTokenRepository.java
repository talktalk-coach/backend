package com.codit.talktalkcoach.repository;

import com.codit.talktalkcoach.domain.entity.RefreshToken;
import com.codit.talktalkcoach.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByTokenValue(String tokenValue);
    Optional<RefreshToken> findByUser(User user);
    void deleteByUser(User user);
}
