package com.codit.talktalkcoach.service;

import com.codit.talktalkcoach.domain.entity.*;
import com.codit.talktalkcoach.domain.enums.SpeechStatus;
import com.codit.talktalkcoach.dto.response.home.HomeResponse;
import com.codit.talktalkcoach.external.gpt.GptClient;
import com.codit.talktalkcoach.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final ObjectMapper objectMapper;

    private static final DateTimeFormatter MONTH_FMT = DateTimeFormatter.ofPattern("yyyy-MM");

    // ─── 홈 화면 통합 데이터 ────────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public HomeResponse getHome(User user) {
        YearMonth thisMonth = YearMonth.now();
        LocalDateTime monthStart = thisMonth.atDay(1).atStartOfDay();
        LocalDateTime monthEnd   = thisMonth.atEndOfMonth().plusDays(1).atStartOfDay();

        // 이번 달 COMPLETED 스피치
        List<Speech> monthSpeeches = speechRepository.findByUserAndStatusAndCreatedAtBetween(
                user, SpeechStatus.COMPLETED, monthStart, monthEnd);

        // 1. radarAverage: 최근 5개 기준
        List<Speech> top5 = speechRepository.findTop10ByUserOrderByCreatedAtDesc(user)
                .stream()
                .filter(s -> s.getStatus() == SpeechStatus.COMPLETED)
                .limit(5)
                .collect(Collectors.toList());

        List<SpeechAnalysis> top5Analyses = top5.stream()
                .map(s -> speechAnalysisRepository.findBySpeechSpeechId(s.getSpeechId()).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        HomeResponse.RadarAverageDto radarAverage = buildRadarAverage(top5Analyses);

        // 2. monthlyScores: [이번 달, 직전 기록이 있는 달]
        List<HomeResponse.MonthlyScoreDto> monthlyScores =
                buildMonthlyScores(user, monthSpeeches, thisMonth);

        // 3. todayPracticeMinutes: 이번 달 누적 분
        int totalSeconds = monthSpeeches.stream().mapToInt(Speech::getDuration).sum();
        int todayPracticeMinutes = totalSeconds / 60;

        // 4. totalCount: 이번 달 스피치 개수
        int totalCount = monthSpeeches.size();

        // 5. summaryFeedback: 캐시 → List<String>으로 파싱
        List<String> summaryFeedback = dashboardStatsRepository.findByUser(user)
                .map(s -> parseFeedbackList(s.getSummaryFeedback()))
                .orElse(null);

        return HomeResponse.builder()
                .radarAverage(radarAverage)
                .monthlyScores(monthlyScores)
                .todayPracticeMinutes(todayPracticeMinutes)
                .summaryFeedback(summaryFeedback)
                .totalCount(totalCount)
                .build();
    }

    // ─── 월간 점수 2개 반환 ──────────────────────────────────────────────────────
    /**
     * [이번 달, 직전에 기록이 있는 달] 순서로 최대 2개 반환
     *
     * 케이스별 처리:
     * 1. 이번 달 데이터 없음   → message: "시작이 반이다! 오늘 첫 스피치에 도전해보세요 🎤"
     * 2. 이번 달만 있고 이전 없음 → 이번 달 1개만 반환 + message: "꾸준한 노력으로 성장하고 있어요!"
     * 3. 이번 달 + 직전 달 있음 → 이번 달 + 직전 달 2개 반환
     * 4. 이번 달 없고 이전 달 있음 → message + 직전 달 반환
     */
    private List<HomeResponse.MonthlyScoreDto> buildMonthlyScores(
            User user, List<Speech> monthSpeeches, YearMonth thisMonth) {

        // 이번 달 데이터 집계
        List<SpeechAnalysis> thisMonthAnalyses = monthSpeeches.stream()
                .map(s -> speechAnalysisRepository.findBySpeechSpeechId(s.getSpeechId()).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        HomeResponse.MonthlyScoreDto thisMonthDto = null;
        if (!thisMonthAnalyses.isEmpty()) {
            double avg = thisMonthAnalyses.stream()
                    .mapToDouble(SpeechAnalysis::calculateAverageScore)
                    .average().orElse(0.0);
            thisMonthDto = HomeResponse.MonthlyScoreDto.builder()
                    .yearMonth(thisMonth.format(MONTH_FMT))
                    .averageScore(round1(avg))
                    .practiceCount(thisMonthAnalyses.size())
                    .build();
        }

        // 이번 달 이전 기록 중 가장 최근 달 조회 (최대 12달 전까지 탐색)
        HomeResponse.MonthlyScoreDto prevDto = null;
        for (int i = 1; i <= 12; i++) {
            YearMonth prevMonth = thisMonth.minusMonths(i);
            LocalDateTime prevStart = prevMonth.atDay(1).atStartOfDay();
            LocalDateTime prevEnd   = prevMonth.atEndOfMonth().plusDays(1).atStartOfDay();

            List<Speech> prevSpeeches = speechRepository.findByUserAndStatusAndCreatedAtBetween(
                    user, SpeechStatus.COMPLETED, prevStart, prevEnd);

            if (!prevSpeeches.isEmpty()) {
                List<SpeechAnalysis> prevAnalyses = prevSpeeches.stream()
                        .map(s -> speechAnalysisRepository
                                .findBySpeechSpeechId(s.getSpeechId()).orElse(null))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

                if (!prevAnalyses.isEmpty()) {
                    double prevAvg = prevAnalyses.stream()
                            .mapToDouble(SpeechAnalysis::calculateAverageScore)
                            .average().orElse(0.0);
                    prevDto = HomeResponse.MonthlyScoreDto.builder()
                            .yearMonth(prevMonth.format(MONTH_FMT))
                            .averageScore(round1(prevAvg))
                            .practiceCount(prevAnalyses.size())
                            .build();
                    break;
                }
            }
        }

        List<HomeResponse.MonthlyScoreDto> result = new ArrayList<>();

        // ── 케이스 분기 ─────────────────────────────────────────────────────
        if (thisMonthDto == null && prevDto == null) {
            // 이번 달 기록도 없고 이전 기록도 없음 (이번 달 처음 가입한 사용자)
            result.add(HomeResponse.MonthlyScoreDto.builder()
                    .message("꾸준한 노력으로 성장하고 있어요!")
                    .build());

        } else if (thisMonthDto == null) {
            // 이번 달 기록 없음, 이전 기록 있음
            result.add(HomeResponse.MonthlyScoreDto.builder()
                    .message("시작이 반이다! 오늘 첫 스피치에 도전해보세요 🎤")
                    .build());
            result.add(prevDto);

        } else if (prevDto == null) {
            // 이번 달 기록만 있고 이전 기록 없음
            result.add(thisMonthDto);
            result.add(HomeResponse.MonthlyScoreDto.builder()
                    .message("꾸준한 노력으로 성장하고 있어요!")
                    .build());

        } else {
            // 이번 달 + 직전 달 모두 있음
            result.add(thisMonthDto);
            result.add(prevDto);
        }

        return result;
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

        String context = buildFeedbackContext(recent5);
        List<String> feedbackList;
        try {
            feedbackList = gptClient.generateSummaryFeedback(context, user.getTargetLevel());
            log.info("AI 피드백 생성 완료: {}개", feedbackList.size());
        } catch (Exception e) {
            log.warn("AI 피드백 생성 실패: {}", e.getMessage());
            return;
        }

        try {
            String json = objectMapper.writeValueAsString(feedbackList);
            DashboardStats stats = dashboardStatsRepository.findByUser(user).orElse(null);
            if (stats == null) {
                stats = dashboardStatsRepository.save(DashboardStats.builder().user(user).build());
            }
            stats.updateFeedback(json);
            dashboardStatsRepository.save(stats);
        } catch (Exception e) {
            log.error("피드백 저장 실패: {}", e.getMessage());
        }
    }

    // ─── DB에서 꺼낸 JSON 문자열 → List<String> 파싱 ────────────────────────────
    private List<String> parseFeedbackList(String json) {
        if (json == null || json.isBlank()) return null;
        try {
            if (json.trim().startsWith("[")) {
                String[] arr = objectMapper.readValue(json, String[].class);
                return Arrays.asList(arr);
            }
            return List.of(json); // 구버전 단일 문자열 호환
        } catch (Exception e) {
            log.warn("summaryFeedback 파싱 실패, 단일 문자열로 반환: {}", e.getMessage());
            return List.of(json);
        }
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

    // ─── GPT 컨텍스트 구성 ───────────────────────────────────────────────────────
    private String buildFeedbackContext(List<Speech> speeches) {
        StringBuilder sb = new StringBuilder("최근 스피치 분석 결과:\n\n");
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
