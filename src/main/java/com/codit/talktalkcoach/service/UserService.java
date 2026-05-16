package com.codit.talktalkcoach.service;

import com.codit.talktalkcoach.domain.entity.User;
import com.codit.talktalkcoach.domain.enums.TargetLevel;
import com.codit.talktalkcoach.dto.response.user.UserInfoResponse;
import com.codit.talktalkcoach.exception.custom.UserNotFoundException;
import com.codit.talktalkcoach.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserInfoResponse getMyInfo(Long userId) {
        User user = findById(userId);
        return UserInfoResponse.from(user);
    }

    @Transactional
    public void updateNickname(Long userId, String nickname) {
        User user = findById(userId);
        user.updateNickname(nickname);
    }

    @Transactional
    public void updateProfileImage(Long userId, String imageUrl) {
        User user = findById(userId);
        user.updateProfileImage(imageUrl);
    }

    @Transactional
    public void updateTargetLevel(Long userId, TargetLevel targetLevel) {
        User user = findById(userId);
        user.updateTargetLevel(targetLevel);
    }

    @Transactional
    public void withdraw(Long userId) {
        User user = findById(userId);
        userRepository.delete(user);    // @SQLDelete → soft delete
    }

    // ─── internal ───────────────────────────────────────────────────────────────
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
    }
}
