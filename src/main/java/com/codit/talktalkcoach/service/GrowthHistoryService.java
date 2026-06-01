package com.codit.talktalkcoach.service;

import com.codit.talktalkcoach.domain.entity.Speech;
import com.codit.talktalkcoach.domain.entity.SpeechAnalysis;
import com.codit.talktalkcoach.domain.entity.User;
import com.codit.talktalkcoach.domain.enums.SpeechStatus;
import com.codit.talktalkcoach.domain.enums.TargetLevel;
import com.codit.talktalkcoach.dto.response.user.GrowthHistoryResponse;
import com.codit.talktalkcoach.repository.SpeechAnalysisRepository;
import com.codit.talktalkcoach.repository.SpeechRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GrowthHistoryService {

    private final SpeechRepository speechRepository;
    private final SpeechAnalysisRepository speechAnalysisRepository;

    /**
     * 유저의 전체 스피치를 targetLevel별로 분류하여
     * 날짜별 평균 점수를 int 배열로 반환한다.
     *
     * - COMPLETED 상태인 스피치만 포함
     * - 하루에 여러 번 스피치한 경우 해당 날짜의 평균 1개로 집계
     * - 최신순 정렬 (앞 = 최근, 뒤 = 오래된 순)
     * - 데이터가 없는 레벨은 응답에서 제외
     */
    @Transactional(readOnly = true)
    public List<GrowthHistoryResponse> getGrowthHistory(User user) {

        // 1. 유저의 전체 COMPLETED 스피치 조회 (날짜 오름차순)
        List<Speech> speeches = speechRepository
                .findByUserAndStatusOrderByCreatedAtAsc(user, SpeechStatus.COMPLETED);

        // 2. targetLevel별로 그룹핑
        Map<TargetLevel, List<Speech>> grouped = speeches.stream()
                .filter(s -> s.getTargetLevel() != null)
                .collect(Collectors.groupingBy(
                        Speech::getTargetLevel,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        List<GrowthHistoryResponse> result = new ArrayList<>();

        // 3. TargetLevel enum 선언 순서대로 처리
        Arrays.stream(TargetLevel.values()).forEach(level -> {
            List<Speech> levelSpeeches = grouped.get(level);
            if (levelSpeeches == null || levelSpeeches.isEmpty()) return;

            // 4. 날짜별로 스피치 그룹핑 (yyyy-MM-dd 기준)
            //    TreeMap → 날짜 오름차순 자동 정렬
            Map<LocalDate, List<Double>> scoresByDate = new TreeMap<>();

            for (Speech speech : levelSpeeches) {
                speechAnalysisRepository.findBySpeechSpeechId(speech.getSpeechId())
                        .ifPresent(analysis -> {
                            LocalDate date = speech.getCreatedAt().toLocalDate();
                            scoresByDate
                                    .computeIfAbsent(date, k -> new ArrayList<>())
                                    .add(analysis.calculateAverageScore());
                        });
            }

            if (scoresByDate.isEmpty()) return;

            // 5. 날짜별 평균 → int 변환 후 최신순(역순) 정렬
            List<Integer> scores = scoresByDate.values().stream()
                    .map(dailyScores -> {
                        double avg = dailyScores.stream()
                                .mapToDouble(Double::doubleValue)
                                .average()
                                .orElse(0.0);
                        return (int) Math.round(avg);
                    })
                    .collect(Collectors.toList());

            // 최신순 (오름차순 → 역순)
            Collections.reverse(scores);

            result.add(GrowthHistoryResponse.builder()
                    .targetLevel(level)
                    .levelLabel(getLevelLabel(level))
                    .scores(scores)
                    .build());
        });

        return result;
    }

    private String getLevelLabel(TargetLevel level) {
        return switch (level) {
            case ELEM_1_2   -> "초등 1~2학년";
            case ELEM_3_4   -> "초등 3~4학년";
            case ELEM_5_6   -> "초등 5~6학년";
            case MIDDLE_1_2 -> "중학교 1~2학년";
            case MIDDLE_3   -> "중학교 3학년";
        };
    }
}
