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
                    .summaryTitle("아직 스피치 기록이 없습니다. 첫 번째 연습을 시작해보세요!")
                    .growthRate(0)
                    .masteryMap(Collections.emptyMap())
                    .dailyScores(buildDailyScores(user))  // 빈 날짜도 채워서 반환
                    .build();
        }

        Map<String, Double> masteryMap = calculateMastery(allSpeeches);
        double growthRate = calculateGrowthRate(allSpeeches);
        List<UserStatisticsResponse.DailyScoreDto> dailyScores = buildDailyScores(user);
        UserStatisticsResponse.TotalScoreDto totalScores = calculateTotalScores(user);

        // 항목별 성장률 계산 (최근 10개 앞5/뒤 5개 비교)
        Map<String, Double> growthRateByItem = calculateItemGrowthRates(allSpeeches);
        String summaryTitle  = buildSummaryTitle(growthRateByItem, allSpeeches.size());
        String summaryDetail = buildSummaryDetail(growthRateByItem, allSpeeches.size());

        return UserStatisticsResponse.builder()
                .summaryTitle(summaryTitle)
                .summaryDetail(summaryDetail)
                .growthRate(growthRate)
                .masteryMap(masteryMap)
                .dailyScores(dailyScores)
                .totalScores(totalScores)
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

    // ─── 일별 점수 (있는 데이터 기준 최대 7개, 부족하면 0점으로 왼쪽 채움) ─────
    private List<UserStatisticsResponse.DailyScoreDto> buildDailyScores(User user) {

        // 1. 전체 COMPLETED 스피치 조회 (오래된 순)
        List<Speech> allSpeeches = speechRepository
                .findByUserAndStatusOrderByCreatedAtAsc(user, SpeechStatus.COMPLETED);

        // 2. 날짜별 평균 집계 (TreeMap → 날짜 오름차순 자동 정렬)
        Map<LocalDate, List<SpeechAnalysis>> byDate = new TreeMap<>();
        for (Speech s : allSpeeches) {
            speechAnalysisRepository.findBySpeechSpeechId(s.getSpeechId()).ifPresent(a -> {
                LocalDate date = s.getCreatedAt().toLocalDate();
                byDate.computeIfAbsent(date, k -> new ArrayList<>()).add(a);
            });
        }

        // 3. 날짜별 DailyScoreDto 생성 (오래된 → 최신 순)
        List<UserStatisticsResponse.DailyScoreDto> dataList = byDate.entrySet().stream()
                .map(entry -> {
                    String dateStr = entry.getKey().format(DATE_FMT);
                    List<SpeechAnalysis> analyses = entry.getValue();
                    return UserStatisticsResponse.DailyScoreDto.builder()
                            .date(dateStr)
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
                })
                .collect(Collectors.toList());

        // 4. 최근 7개만 추출 (7개 초과 시 최신 7개)
        if (dataList.size() > 7) {
            dataList = dataList.subList(dataList.size() - 7, dataList.size());
        }

        // 5. 7개 미만이면 왼쪽(오래된 쪽)에 0점 패딩
        List<UserStatisticsResponse.DailyScoreDto> result = new ArrayList<>();
        int padCount = 7 - dataList.size();
        for (int i = 0; i < padCount; i++) {
            result.add(UserStatisticsResponse.DailyScoreDto.builder()
                    .date(null)   // 날짜 없음 (프론트에서 빈 칸으로 처리)
                    .accuracyScore(0.0).fluencyScore(0.0).prosodyScore(0.0)
                    .vocabularyScore(0.0).logicScore(0.0).structureScore(0.0)
                    .averageScore(0.0)
                    .build());
        }
        result.addAll(dataList);

        return result;  // 항상 7개
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

    // ─── 전체 스피치 항목별 평균 ─────────────────────────────────────
    private UserStatisticsResponse.TotalScoreDto calculateTotalScores(User user) {
        List<Speech> allSpeeches = speechRepository
                .findByUserAndStatusOrderByCreatedAtAsc(user, SpeechStatus.COMPLETED);

        if (allSpeeches.isEmpty()) {
            return UserStatisticsResponse.TotalScoreDto.builder()
                    .accuracyScore(0.0).fluencyScore(0.0).prosodyScore(0.0)
                    .vocabularyScore(0.0).logicScore(0.0).structureScore(0.0)
                    .averageScore(0.0)
                    .build();
        }

        List<SpeechAnalysis> analyses = allSpeeches.stream()
                .map(s -> speechAnalysisRepository.findBySpeechSpeechId(s.getSpeechId())
                        .orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return UserStatisticsResponse.TotalScoreDto.builder()
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

    // ─── 항목별 성장률 계산 (앞 5개 vs 뒤 5개) ─────────────────────────────
    private Map<String, Double> calculateItemGrowthRates(List<Speech> speeches) {
        // speeches는 이미 치루 내림차순 (어린것 맨 뒤)
        // 앞 5개 = 오래된 실력, 뒤 5개 = 최근 실력
        int total = speeches.size();
        int half  = total / 2;

        List<Speech> older  = speeches.subList(half, total);  // 오래된 절반
        List<Speech> recent = speeches.subList(0, half);      // 최근 절반

        List<String> items = List.of(
                "accuracy", "fluency", "prosody", "vocabulary", "logic", "structure");

        Map<String, Double> growthRates = new LinkedHashMap<>();
        for (String item : items) {
            double olderAvg  = avgByItem(older, item);
            double recentAvg = avgByItem(recent, item);

            double rate = (olderAvg == 0) ? 0.0
                    : round1((recentAvg - olderAvg) / olderAvg * 100);
            growthRates.put(item, rate);
        }
        return growthRates;
    }

    private double avgByItem(List<Speech> speeches, String item) {
        return speeches.stream()
                .map(s -> speechAnalysisRepository.findBySpeechSpeechId(s.getSpeechId())
                        .orElse(null))
                .filter(Objects::nonNull)
                .mapToDouble(a -> getScoreByItem(a, item))
                .average().orElse(0.0);
    }

    private double getScoreByItem(SpeechAnalysis a, String item) {
        return switch (item) {
            case "accuracy"   -> a.getAccuracyScore()   != null ? a.getAccuracyScore()   : 0.0;
            case "fluency"    -> a.getFluencyScore()    != null ? a.getFluencyScore()    : 0.0;
            case "prosody"    -> a.getProsodyScore()    != null ? a.getProsodyScore()    : 0.0;
            case "vocabulary" -> a.getVocabularyScore() != null ? a.getVocabularyScore() : 0.0;
            case "logic"      -> a.getLogicScore()      != null ? a.getLogicScore()      : 0.0;
            case "structure"  -> a.getStructureScore()  != null ? a.getStructureScore()  : 0.0;
            default -> 0.0;
        };
    }

    // ─── 항목 한국어 이름 ──────────────────────────────────────────────
    private String toKorean(String item) {
        return switch (item) {
            case "accuracy"   -> "발음";
            case "fluency"    -> "유창성";
            case "prosody"    -> "억양와 리듬";
            case "vocabulary" -> "어휘 다양성";
            case "logic"      -> "논리 구성";
            case "structure"  -> "발표 구조";
            default -> item;
        };
    }

    // ─── summaryTitle 생성 ───────────────────────────────────────────────
    private String buildSummaryTitle(Map<String, Double> growthRates, int speechCount) {
        if (speechCount == 0) return "첫 번째 스피치를 시작해보세요!";
        if (speechCount < 5) return "꼭준히 연습 중이에요! 조금 더 하면 상세한 리포트를 볼 수 있어요.";

        // 성장률 상위 2개 추출
        List<Map.Entry<String, Double>> top2 = growthRates.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(2)
                .collect(Collectors.toList());

        if (top2.isEmpty()) return "꼭준히 연습 중이에요!";

        String first  = toKorean(top2.get(0).getKey());
        double rate1  = top2.get(0).getValue();

        if (top2.size() == 1 || rate1 <= 0) {
            if (rate1 > 0)
                return String.format("%s이(가) 눈에 띄게 성장했어요!", first);
            return "꼭준히 연습하고 있어요. 지금의 페이스를 유지해보세요!";
        }

        String second = toKorean(top2.get(1).getKey());
        double rate2  = top2.get(1).getValue();

        if (rate1 > 0 && rate2 > 0)
            return String.format("%s과(와) %s이(가) 눈에 띄게 성장했어요!", first, second);
        if (rate1 > 0)
            return String.format("%s이(가) 눈에 띄게 성장했어요!", first);
        return "꼭준히 연습하고 있어요. 지금의 페이스를 유지해보세요!";
    }

    // ─── summaryDetail 생성 ──────────────────────────────────────────────
    private String buildSummaryDetail(Map<String, Double> growthRates, int speechCount) {
        if (speechCount < 5) return "더 많이 연습할수록 정확한 성장 분석이 가능해집니다.";

        // 성장률 1위 항목
        Map.Entry<String, Double> top = growthRates.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);

        if (top == null || top.getValue() <= 0)
            return "지속적인 연습으로 성장하고 있어요.";

        return String.format("지난 연습 대비 %s 점수가 %.1f%% 향상되었습니다.",
                toKorean(top.getKey()), top.getValue());
    }

}
