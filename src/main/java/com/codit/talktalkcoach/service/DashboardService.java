package com.codit.talktalkcoach.service;

import com.codit.talktalkcoach.domain.entity.*;
import com.codit.talktalkcoach.domain.enums.SpeechStatus;
import com.codit.talktalkcoach.dto.response.home.HomeResponse;
import com.codit.talktalkcoach.external.gpt.GptClient;
import com.codit.talktalkcoach.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardService {

    private final DashboardStatsRepository dashboardStatsRepository;
    private final MonthlyStatsRepository monthlyStatsRepository;
    private final SpeechRepository speechRepository;
    private final SpeechAnalysisRepository speechAnalysisRepository;
    private final GptClient gptClient;

    private static final DateTimeFormatter MONTH_FMT = DateTimeFormatter.ofPattern("yyyy-MM");

    // ─── 홈 화면 통합 데이터 ────────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public HomeResponse getHome(User user) {

        // 이번 달 범위 계산
        YearMonth thisMonth = YearMonth.now();
        LocalDateTime monthStart = thisMonth.atDay(1).atStartOfDay();
        LocalDateTime monthEnd   = thisMonth.atEndOfMonth().plusDays(1).atStartOfDay();

        // 오늘 범위
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        LocalDateTime todayEnd   = todayStart.plusDays(1);

        // ── 이번 달 전체 COMPLETED 스피치 ─────────────────────────────────────
        List<Speech> monthSpeeches = speechRepository.findByUserAndStatusAndCreatedAtBetween(
                user, SpeechStatus.COMPLETED, monthStart, monthEnd);

        // ── 1. radarAverage: 최근 5개 기준 지표별 평균 ────────────────────────
        List<Speech> top5 = speechRepository.findTop10ByUserOrderByCreatedAtDesc(user)
                .stream()
                .filter(s -> s.getStatus() == SpeechStatus.COMPLETED)
                .limit(5)
                .collect(Collectors.toList());

        List<SpeechAnalysis> top5Analyses = top5.stream()
                .map(s -> speechAnalysisRepository.findBySpeechSpeechId(s.getSpeechId())
                        .orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        HomeResponse.RadarAverageDto radarAverage = buildRadarAverage(top5Analyses);

        // ── 2. monthlyScores: 이번 달 전체 스피치 평균 ────────────────────────
        List<SpeechAnalysis> monthAnalyses = monthSpeeches.stream()
                .map(s -> speechAnalysisRepository.findBySpeechSpeechId(s.getSpeechId())
                        .orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        HomeResponse.MonthlyScoreDto currentMonthScore = null;
        if (!monthAnalyses.isEmpty()) {
            double monthAvg = monthAnalyses.stream()
                    .mapToDouble(SpeechAnalysis::calculateAverageScore)
                    .average().orElse(0.0);
            currentMonthScore = HomeResponse.MonthlyScoreDto.builder()
                    .yearMonth(thisMonth.format(MONTH_FMT))
                    .averageScore(round1(monthAvg))
                    .practiceCount(monthAnalyses.size())
                    .build();
        }

        List<HomeResponse.MonthlyScoreDto> monthlyScores = currentMonthScore != null
                ? List.of(currentMonthScore)
                : Collections.emptyList();

        // ── 3. todayPracticeMinutes: 이번 달 전체 스피치 duration 합산 → 분 단위 ─
        //    (오늘 달성 시간이므로 이번 달 = 현재 달의 누적 연습 시간을 분으로 환산)
        int totalSecondsThisMonth = monthSpeeches.stream()
                .mapToInt(Speech::getDuration)
                .sum();
        int todayPracticeMinutes = totalSecondsThisMonth / 60;

        // ── 4. totalCount: 이번 달 스피치 개수 ───────────────────────────────
        int totalCount = monthSpeeches.size();

        // ── 5. summaryFeedback: DashboardStats 캐시에서 가져옴 ────────────────
        String summaryFeedback = dashboardStatsRepository.findByUser(user)
                .map(DashboardStats::getSummaryFeedback)
                .orElse(null);

        return HomeResponse.builder()
                .radarAverage(radarAverage)
                .monthlyScores(monthlyScores)
                .todayPracticeMinutes(todayPracticeMinutes)
                .summaryFeedback(summaryFeedback)
                .totalCount(totalCount)
                .build();
    }

    // ─── 레이더 평균 계산 ────────────────────────────────────────────────────────
    private HomeResponse.RadarAverageDto buildRadarAverage(List<SpeechAnalysis> analyses) {
        if (analyses.isEmpty()) {
            return HomeResponse.RadarAverageDto.builder()
                    .basedOnCount(0)
                    .accuracyScore(0.0).fluencyScore(0.0).prosodyScore(0.0)
                    .vocabularyScore(0.0).logicScore(0.0).structureScore(0.0)
                    .averageScore(0.0)
                    .build();
        }
        return HomeResponse.RadarAverageDto.builder()
                .basedOnCount(analyses.size())
                .accuracyScore(round1(avg(analyses, SpeechAnalysis::getAccuracyScore)))
                .fluencyScore(round1(avg(analyses, SpeechAnalysis::getFluencyScore)))
                .prosodyScore(round1(avg(analyses, SpeechAnalysis::getProsodyScore)))
                .vocabularyScore(round1(avg(analyses, SpeechAnalysis::getVocabularyScore)))
                .logicScore(round1(avg(analyses, SpeechAnalysis::getLogicScore)))
                .structureScore(round1(avg(analyses, SpeechAnalysis::getStructureScore)))
                .averageScore(round1(analyses.stream()
                        .mapToDouble(SpeechAnalysis::calculateAverageScore)
                        .average().orElse(0)))
                .build();
    }

    // ─── refreshStats: 스피치 분석 완료 후 AI 피드백 갱신 ────────────────────────
    @Transactional
    public void refreshStats(User user) {
        List<Speech> recent5 = speechRepository.findTop10ByUserOrderByCreatedAtDesc(user)
                .stream()
                .filter(s -> s.getStatus() == SpeechStatus.COMPLETED)
                .limit(5)
                .collect(Collectors.toList());

        if (recent5.isEmpty()) return;

        // GPT 피드백 생성
        String context = buildFeedbackContext(recent5);
        String summaryFeedback;
        try {
            summaryFeedback = gptClient.generateSummaryFeedback(context, user.getTargetLevel());
            log.info("AI 피드백 생성 완료: {}", summaryFeedback);
        } catch (Exception e) {
            log.warn("AI 피드백 생성 실패: {}", e.getMessage());
            summaryFeedback = null;
        }

        // ✅ DashboardStats 저장 — findOrCreate 후 반드시 save() 호출
        DashboardStats stats = dashboardStatsRepository.findByUser(user)
                .orElse(null);

        if (stats == null) {
            stats = dashboardStatsRepository.save(
                    DashboardStats.builder().user(user).build());
        }

        if (summaryFeedback != null) {
            stats.updateFeedback(summaryFeedback);
            dashboardStatsRepository.save(stats);   // 명시적 save
        }
    }

    // ─── GPT 컨텍스트 구성 ───────────────────────────────────────────────────────
    private String buildFeedbackContext(List<Speech> speeches) {
        StringBuilder sb = new StringBuilder("최근 스피치 5개 분석 결과:\n\n");
        int[] idx = {1};
        speeches.forEach(s ->
                speechAnalysisRepository.findBySpeechSpeechId(s.getSpeechId()).ifPresent(a -> {
                    sb.append(String.format(
                            "[%d회] 평균:%.1f | 정확도:%.1f | 유창성:%.1f | 어휘:%.1f | 논리:%.1f | 구조:%.1f\n",
                            idx[0]++,
                            a.calculateAverageScore(),
                            orZero(a.getAccuracyScore()),
                            orZero(a.getFluencyScore()),
                            orZero(a.getVocabularyScore()),
                            orZero(a.getLogicScore()),
                            orZero(a.getStructureScore())
                    ));
                })
        );
        sb.append("\n위 데이터를 바탕으로 구체적인 통합 피드백 2가지를 작성해주세요.");
        return sb.toString();
    }

    // ─── 유틸 ────────────────────────────────────────────────────────────────────
    private double avg(List<SpeechAnalysis> list,
                       java.util.function.Function<SpeechAnalysis, Double> fn) {
        return list.stream().map(fn).filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    private double round1(double v) { return Math.round(v * 10.0) / 10.0; }

    private double orZero(Double v) { return v != null ? v : 0.0; }
}
