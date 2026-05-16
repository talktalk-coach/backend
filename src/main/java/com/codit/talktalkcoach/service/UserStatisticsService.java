package com.codit.talktalkcoach.service;

import com.codit.talktalkcoach.domain.entity.*;
import com.codit.talktalkcoach.domain.enums.SpeechStatus;
import com.codit.talktalkcoach.dto.response.user.UserStatisticsResponse;
import com.codit.talktalkcoach.repository.SpeechAnalysisRepository;
import com.codit.talktalkcoach.repository.SpeechRepository;
import com.codit.talktalkcoach.util.ScoreCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserStatisticsService {

    private final SpeechRepository speechRepository;
    private final SpeechAnalysisRepository speechAnalysisRepository;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Transactional(readOnly = true)
    public UserStatisticsResponse getStatistics(User user) {
        List<Speech> allSpeeches = speechRepository.findTop10ByUserOrderByCreatedAtDesc(user)
                .stream()
                .filter(s -> s.getStatus() == SpeechStatus.COMPLETED)
                .collect(Collectors.toList());

        if (allSpeeches.isEmpty()) {
            return UserStatisticsResponse.builder()
                    .summaryLine("아직 스피치 기록이 없습니다. 첫 번째 연습을 시작해보세요!")
                    .growthRate(0)
                    .masteryMap(Collections.emptyMap())
                    .dailyScores(buildDailyScores(user))  // 빈 날짜도 채워서 반환
                    .build();
        }

        Map<String, Double> masteryMap = calculateMastery(allSpeeches);
        double growthRate = calculateGrowthRate(allSpeeches);
        List<UserStatisticsResponse.DailyScoreDto> dailyScores = buildDailyScores(user);
        String summaryLine = buildSummaryLine(masteryMap, growthRate);

        return UserStatisticsResponse.builder()
                .summaryLine(summaryLine)
                .growthRate(growthRate)
                .masteryMap(masteryMap)
                .dailyScores(dailyScores)
                .build();
    }

    // ─── 핵심역량 마스터리 ────────────────────────────────────────────────────
    private Map<String, Double> calculateMastery(List<Speech> speeches) {
        Map<String, List<Double>> scoreMap = new LinkedHashMap<>();
        scoreMap.put("accuracy",   new ArrayList<>());
        scoreMap.put("fluency",    new ArrayList<>());
        scoreMap.put("prosody",    new ArrayList<>());
        scoreMap.put("vocabulary", new ArrayList<>());
        scoreMap.put("logic",      new ArrayList<>());
        scoreMap.put("structure",  new ArrayList<>());

        speeches.forEach(s -> speechAnalysisRepository.findBySpeechSpeechId(s.getSpeechId())
                .ifPresent(a -> {
                    if (a.getAccuracyScore()  != null) scoreMap.get("accuracy").add(a.getAccuracyScore());
                    if (a.getFluencyScore()   != null) scoreMap.get("fluency").add(a.getFluencyScore());
                    if (a.getProsodyScore()   != null) scoreMap.get("prosody").add(a.getProsodyScore());
                    if (a.getVocabularyScore()!= null) scoreMap.get("vocabulary").add(a.getVocabularyScore());
                    if (a.getLogicScore()     != null) scoreMap.get("logic").add(a.getLogicScore());
                    if (a.getStructureScore() != null) scoreMap.get("structure").add(a.getStructureScore());
                }));

        return scoreMap.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> e.getValue().isEmpty() ? 0.0
                        : round1(e.getValue().stream().mapToDouble(Double::doubleValue)
                        .average().orElse(0)),
                (a, b) -> a, LinkedHashMap::new));
    }

    // ─── 성장률 ───────────────────────────────────────────────────────────────
    private double calculateGrowthRate(List<Speech> speeches) {
        if (speeches.size() < 2) return 0.0;

        List<Double> avgs = speeches.stream()
                .map(s -> speechAnalysisRepository.findBySpeechSpeechId(s.getSpeechId())
                        .map(SpeechAnalysis::calculateAverageScore).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (avgs.size() < 2) return 0.0;
        int half = avgs.size() / 2;

        double recent   = avgs.subList(0, half).stream()
                .mapToDouble(Double::doubleValue).average().orElse(0);
        double previous = avgs.subList(half, avgs.size()).stream()
                .mapToDouble(Double::doubleValue).average().orElse(0);

        return ScoreCalculator.calculateGrowthRate(previous, recent);
    }

    // ─── 최근 7일 일별 점수 (보간 포함) ──────────────────────────────────────
    private List<UserStatisticsResponse.DailyScoreDto> buildDailyScores(User user) {
        LocalDate today   = LocalDate.now();
        LocalDate weekAgo = today.minusDays(6);  // 7일치 (오늘 포함)

        // 1. 최근 7일 + 그 이전 마지막 스피치까지 조회 (보간 기준값 확보용)
        LocalDateTime from = weekAgo.minusDays(30).atStartOfDay(); // 여유있게 30일 전부터
        LocalDateTime to   = today.plusDays(1).atStartOfDay();

        List<Speech> speeches = speechRepository
                .findByUserAndStatusAndCreatedAtBetween(user, SpeechStatus.COMPLETED, from, to);

        // 2. 날짜별 실제 스피치 평균 집계 (날짜 → averageScore)
        Map<LocalDate, Double> actualScores = new TreeMap<>();
        speeches.forEach(s -> {
            LocalDate date = s.getCreatedAt().toLocalDate();
            speechAnalysisRepository.findBySpeechSpeechId(s.getSpeechId()).ifPresent(a -> {
                double score = a.calculateAverageScore();
                actualScores.merge(date, score, (old, nw) -> (old + nw) / 2.0);
            });
        });

        // 3. 일주일 내 실제 데이터가 있는 날짜만 추출 (정렬)
        List<LocalDate> actualDatesInWeek = actualScores.keySet().stream()
                .filter(d -> !d.isBefore(weekAgo) && !d.isAfter(today))
                .sorted()
                .collect(Collectors.toList());

        // 4. 일주일 범위 밖 이전 데이터 중 가장 최근 값 (7일 시작 이전 기준점)
        OptionalDouble beforeWeekScore = actualScores.entrySet().stream()
                .filter(e -> e.getKey().isBefore(weekAgo))
                .max(Map.Entry.comparingByKey())
                .map(e -> OptionalDouble.of(e.getValue()))
                .orElse(OptionalDouble.empty());

        // 5. 일주일 전체 7일치 채우기
        List<UserStatisticsResponse.DailyScoreDto> result = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            LocalDate date = weekAgo.plusDays(i);
            String dateStr = date.format(DATE_FMT);

            if (actualScores.containsKey(date)) {
                // 실제 스피치가 있는 날 → 풀 데이터
                Speech refSpeech = speeches.stream()
                        .filter(s -> s.getCreatedAt().toLocalDate().equals(date))
                        .findFirst().orElse(null);

                if (refSpeech != null) {
                    speechAnalysisRepository.findBySpeechSpeechId(refSpeech.getSpeechId())
                            .ifPresent(a -> {
                                // 같은 날 여러 스피치는 평균으로
                                List<SpeechAnalysis> dayAnalyses = speeches.stream()
                                        .filter(s2 -> s2.getCreatedAt().toLocalDate().equals(date))
                                        .map(s2 -> speechAnalysisRepository
                                                .findBySpeechSpeechId(s2.getSpeechId()).orElse(null))
                                        .filter(Objects::nonNull)
                                        .collect(Collectors.toList());

                                result.add(UserStatisticsResponse.DailyScoreDto.builder()
                                        .date(dateStr)
                                        .accuracyScore(round1(avg(dayAnalyses, SpeechAnalysis::getAccuracyScore)))
                                        .fluencyScore(round1(avg(dayAnalyses, SpeechAnalysis::getFluencyScore)))
                                        .prosodyScore(round1(avg(dayAnalyses, SpeechAnalysis::getProsodyScore)))
                                        .vocabularyScore(round1(avg(dayAnalyses, SpeechAnalysis::getVocabularyScore)))
                                        .logicScore(round1(avg(dayAnalyses, SpeechAnalysis::getLogicScore)))
                                        .structureScore(round1(avg(dayAnalyses, SpeechAnalysis::getStructureScore)))
                                        .averageScore(round1(dayAnalyses.stream()
                                                .mapToDouble(SpeechAnalysis::calculateAverageScore)
                                                .average().orElse(0)))
                                        .build());
                            });
                }
            } else {
                // 스피치가 없는 날 → 보간 또는 특수 처리
                double interpolated = interpolate(date, actualDatesInWeek, actualScores, beforeWeekScore, today);
                result.add(UserStatisticsResponse.DailyScoreDto.builder()
                        .date(dateStr)
                        .accuracyScore(interpolated)
                        .fluencyScore(interpolated)
                        .prosodyScore(interpolated)
                        .vocabularyScore(interpolated)
                        .logicScore(interpolated)
                        .structureScore(interpolated)
                        .averageScore(interpolated)
                        .build());
            }
        }

        return result;
    }

    /**
     * 스피치가 없는 날짜의 보간값 계산
     *
     * 규칙:
     * 1. 앞뒤 실제 데이터가 있으면 → 선형 보간
     * 2. 첫 데이터 이전 (또는 일주일 범위 이전 기준값) → 첫 기록값
     * 3. 마지막 데이터 이후 (미래 or 오늘) → 0
     * 4. 일주일 내 데이터가 전혀 없고 이전 기준값 있음 → 이전 기준값
     * 5. 모든 데이터 없음 → 0
     */
    private double interpolate(LocalDate date,
                                List<LocalDate> actualDatesInWeek,
                                Map<LocalDate, Double> actualScores,
                                OptionalDouble beforeWeekScore,
                                LocalDate today) {
        // 일주일 내 실제 데이터가 없는 경우
        if (actualDatesInWeek.isEmpty()) {
            // 이전 기준값이 있으면 그 값 유지
            return beforeWeekScore.isPresent() ? round1(beforeWeekScore.getAsDouble()) : 0.0;
        }

        LocalDate firstActual = actualDatesInWeek.get(0);
        LocalDate lastActual  = actualDatesInWeek.get(actualDatesInWeek.size() - 1);

        // 첫 실제 데이터보다 이전 → 첫 기록값 (또는 이전 기준값)
        if (date.isBefore(firstActual)) {
            if (beforeWeekScore.isPresent()) {
                return round1(beforeWeekScore.getAsDouble());
            }
            return round1(actualScores.get(firstActual));
        }

        // 마지막 실제 데이터보다 이후 → 0 (아직 진행 안 한 날)
        if (date.isAfter(lastActual)) {
            return 0.0;
        }

        // 앞뒤 실제 데이터 찾아서 선형 보간
        LocalDate prev = null, next = null;
        for (LocalDate d : actualDatesInWeek) {
            if (!d.isAfter(date)) prev = d;
            if (!d.isBefore(date) && next == null) next = d;
        }

        if (prev == null) return round1(actualScores.get(next));
        if (next == null || next.equals(prev)) return round1(actualScores.get(prev));

        double prevScore = actualScores.get(prev);
        double nextScore = actualScores.get(next);
        long totalDays   = prev.until(next, java.time.temporal.ChronoUnit.DAYS);
        long elapsedDays = prev.until(date, java.time.temporal.ChronoUnit.DAYS);

        double interpolated = prevScore + (nextScore - prevScore) * ((double) elapsedDays / totalDays);
        return round1(interpolated);
    }

    // ─── 유틸 ─────────────────────────────────────────────────────────────────
    private Double avg(List<SpeechAnalysis> list,
                       java.util.function.Function<SpeechAnalysis, Double> fn) {
        return list.stream().map(fn).filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue).average().orElse(0);
    }

    private double round1(double v) {
        return Math.round(v * 10.0) / 10.0;
    }

    private String buildSummaryLine(Map<String, Double> mastery, double growthRate) {
        String best = mastery.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).orElse("전체");
        return String.format("가장 강한 역량은 [%s] 이며, 최근 성장률은 %.1f%%입니다.", best, growthRate);
    }
}
