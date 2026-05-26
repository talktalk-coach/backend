package com.codit.talktalkcoach.service;

import com.codit.talktalkcoach.domain.entity.Speech;
import com.codit.talktalkcoach.domain.entity.User;
import com.codit.talktalkcoach.domain.enums.SpeechCategory;
import com.codit.talktalkcoach.repository.SpeechRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Speech 저장 전용 빈
 * 분리 이유:
 *   SpeechService.uploadAndAnalyze()에서 @Transactional 범위 안에
 *   analyzeAsync()를 호출하면, HTTP 스레드의 트랜잭션이 커밋되기 전에
 *   비동기 스레드가 speechId로 DB를 조회하여 "존재하지 않는 스피치" 오류 발생.
 *   이 빈의 save()는 호출 즉시 독립 트랜잭션으로 커밋되므로,
 *   save() 반환 시점에 RDS에 Speech가 확정된 상태가 보장됨.
 *   그 이후 analyzeAsync()를 호출하면 타이밍 문제가 사라짐.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SpeechSaveService {

    private final SpeechRepository speechRepository;

    /**
     * Speech 엔티티를 저장하고 즉시 커밋한다.
     * REQUIRES_NEW: 외부 트랜잭션과 무관하게 독립적으로 커밋됨.
     */
    @Transactional(propagation = org.springframework.transaction.annotation.Propagation.REQUIRES_NEW)
    public Speech save(User user, String title, String audioUrl,
                       int duration, SpeechCategory category) {
        Speech speech = Speech.builder()
                .user(user)
                .title(title)
                .audioUrl(audioUrl)
                .duration(duration)
                .targetLevel(user.getTargetLevel())
                .category(category)
                .build();
        Speech saved = speechRepository.save(speech);
        log.info("Speech 저장 완료 (즉시 커밋) - speechId: {}", saved.getSpeechId());
        return saved;
    }
}
