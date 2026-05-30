package com.codit.talktalkcoach.service;

import com.codit.talktalkcoach.domain.entity.DailyQuizLog;
import com.codit.talktalkcoach.domain.entity.DailyWord;
import com.codit.talktalkcoach.domain.entity.User;
import com.codit.talktalkcoach.domain.enums.TargetLevel;
import com.codit.talktalkcoach.dto.request.quiz.QuizSubmitRequest;
import com.codit.talktalkcoach.dto.response.quiz.DailyQuizResponse;
import com.codit.talktalkcoach.exception.BusinessException;
import com.codit.talktalkcoach.exception.ErrorCode;
import com.codit.talktalkcoach.repository.DailyQuizLogRepository;
import com.codit.talktalkcoach.repository.DailyWordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DailyQuizService {

    private final DailyWordRepository dailyWordRepository;
    private final DailyQuizLogRepository dailyQuizLogRepository;
    private final DailyQuizSchedulerService schedulerService;

    // ─── 오늘의 퀴즈 3개 조회 ─────────────────────────────────────────────────
    @Transactional
    public List<DailyQuizResponse> getTodayQuiz(User user) {
        TargetLevel level = user.getTargetLevel();
        if (level == null) level = TargetLevel.ELEM_3_4;

        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        LocalDateTime todayEnd   = todayStart.plusDays(1);

        List<DailyWord> words = dailyWordRepository.findTodayQuizByLevel(level, todayStart, todayEnd);

        if (words.isEmpty()) {
            log.info("오늘 퀴즈 없음. 즉시 생성 시작 - level: {}", level);
            try {
                schedulerService.generateQuizForLevel(level);
                words = dailyWordRepository.findTodayQuizByLevel(level, todayStart, todayEnd);
            } catch (Exception e) {
                log.error("즉시 퀴즈 생성 실패: {}", e.getMessage());
                return Collections.emptyList();
            }
        }

        // 오늘 정답으로 제출한 퀴즈 로그만 조회 (오답은 로그 없음)
        Map<Long, DailyQuizLog> answeredMap = dailyQuizLogRepository
                .findByUserAndAnsweredAtBetween(user, todayStart, todayEnd)
                .stream()
                .collect(Collectors.toMap(
                        log -> log.getDailyWord().getWordId(),
                        log -> log,
                        (a, b) -> a));

        return words.stream()
                .map(word -> {
                    List<String> options = new ArrayList<>(
                            Arrays.asList(word.getAnswer(), word.getOption2(), word.getOption3()));
                    Collections.shuffle(options);

                    DailyQuizLog log = answeredMap.get(word.getWordId());
                    boolean answered = log != null; // 정답 제출 여부

                    return DailyQuizResponse.builder()
                            .wordId(word.getWordId())
                            .description(word.getDescription())
                            .options(options)
                            .answered(answered)
                            .isCorrect(answered ? log.isCorrect() : null)
                            .build();
                })
                .collect(Collectors.toList());
    }

    // ─── 퀴즈 정답 제출 ────────────────────────────────────────────────────────
    @Transactional
    public Map<String, Object> submitAnswer(User user, QuizSubmitRequest request) {
        DailyWord word = dailyWordRepository.findById(request.getWordId())
                .orElseThrow(() -> new BusinessException(ErrorCode.QUIZ_NOT_FOUND));

        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        LocalDateTime todayEnd   = todayStart.plusDays(1);

        // 이미 정답으로 제출한 경우에만 중복 차단
        // 오답은 로그 저장 안 하므로 재제출 가능
        boolean alreadyCorrect = dailyQuizLogRepository
                .findByUserAndDailyWordAndAnsweredAtBetween(user, word, todayStart, todayEnd)
                .isPresent();

        if (alreadyCorrect) {
            throw new BusinessException(ErrorCode.QUIZ_ALREADY_ANSWERED);
        }

        boolean isCorrect = word.getAnswer().trim().equals(request.getSelectedOption().trim());

        if (isCorrect) {
            // 정답일 때만 로그 저장 → 이후 재제출 차단
            DailyQuizLog quizLog = DailyQuizLog.builder()
                    .user(user)
                    .dailyWord(word)
                    .isCorrect(true)
                    .build();
            dailyQuizLogRepository.save(quizLog);
        }
        // 오답일 때는 로그 저장 안 함 → 재제출 가능

        long correctCount = dailyQuizLogRepository.countTodayCorrect(user, todayStart, todayEnd);

        return Map.of(
                "isCorrect",        isCorrect,
                "correctAnswer",    isCorrect ? word.getAnswer() : "",  // 오답 시 정답 숨김
                "todayCorrectCount", correctCount,
                "streak",           String.format("%d/3", correctCount)
        );
    }
}
