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
    @Transactional //(readOnly = true)
    public List<DailyQuizResponse> getTodayQuiz(User user) {
        TargetLevel level = user.getTargetLevel();
        if (level == null) level = TargetLevel.ELEM_3_4; // 기본값

        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        LocalDateTime todayEnd   = todayStart.plusDays(1);

        List<DailyWord> words = dailyWordRepository.findTodayQuizByLevel(level, todayStart, todayEnd);

        // 오늘 퀴즈가 없으면 즉시 생성 (최초 접속 또는 스케줄러 미실행)
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

        // 오늘 이미 제출한 퀴즈 로그 조회
        Map<Long, DailyQuizLog> answeredMap = dailyQuizLogRepository
                .findByUserAndAnsweredAtBetween(user, todayStart, todayEnd)
                .stream()
                .collect(Collectors.toMap(
                        log -> log.getDailyWord().getWordId(),
                        log -> log,
                        (a, b) -> a));

        return words.stream()
                .map(word -> {
                    // 선택지 무작위 섞기
                    List<String> options = new ArrayList<>(
                            Arrays.asList(word.getAnswer(), word.getOption2(), word.getOption3()));
                    Collections.shuffle(options);

                    DailyQuizLog log = answeredMap.get(word.getWordId());
                    boolean answered = log != null;

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

        // 오늘 이미 제출했는지 확인
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        LocalDateTime todayEnd   = todayStart.plusDays(1);

        boolean alreadyAnswered = dailyQuizLogRepository
                .findByUserAndDailyWordAndAnsweredAtBetween(user, word, todayStart, todayEnd)
                .isPresent();

        if (alreadyAnswered) {
            throw new BusinessException(ErrorCode.QUIZ_ALREADY_ANSWERED);
        }

        boolean isCorrect = word.getAnswer().trim().equals(request.getSelectedOption().trim());

        DailyQuizLog quizLog = DailyQuizLog.builder()
                .user(user)
                .dailyWord(word)
                .isCorrect(isCorrect)
                .build();
        dailyQuizLogRepository.save(quizLog);

        // 오늘 스트릭 계산 (정답 개수 / 전체 3개)
        long correctCount = dailyQuizLogRepository.countTodayCorrect(user, todayStart, todayEnd);

        return Map.of(
                "isCorrect", isCorrect,
                "correctAnswer", word.getAnswer(),
                "todayCorrectCount", correctCount,
                "streak", String.format("%d/3", correctCount)
        );
    }
}
