package com.codit.talktalkcoach.service;

import com.codit.talktalkcoach.domain.entity.Speech;
import com.codit.talktalkcoach.domain.entity.SpeechAnalysis;
import com.codit.talktalkcoach.domain.entity.User;
import com.codit.talktalkcoach.domain.enums.TargetLevel;
import com.codit.talktalkcoach.dto.response.user.GrowthHistoryResponse;
import com.codit.talktalkcoach.repository.SpeechAnalysisRepository;
import com.codit.talktalkcoach.repository.SpeechRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GrowthHistoryService {

    private final SpeechRepository speechRepository;
    private final SpeechAnalysisRepository speechAnalysisRepository;

    private static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 유저의 전체 스피치를 targetLevel별로 분류하여
     * 각 레벨의 회차별 averageScore 시계열 데이터를 반환한다.
     *
     * - COMPLETED 상태인 스피치만 포함
     * - 날짜 오름차순 정렬 (차트에서 시간 흐름 표현)
     * - 데이터가 없는 레벨은 응답에서 제외
     */
    @Transactional(readOnly = true)
    public List<GrowthHistoryResponse> getGrowthHistory(User user) {

        // 1. 유저의 전체 완료된 스피치 조회 (날짜 오름차순)
        List<Speech> speeches = speechRepository
                .findByUserAndStatusOrderByCreatedAtAsc(user,
                        com.codit.talktalkcoach.domain.enums.SpeechStatus.COMPLETED);

        // 2. targetLevel 별로 그룹핑
        //    LinkedHashMap → TargetLevel enum 선언 순서 유지 (ELEM_1_2 ~ MIDDLE_3)
        Map<TargetLevel, List<Speech>> grouped = speeches.stream()
                .filter(s -> s.getTargetLevel() != null)
                .collect(Collectors.groupingBy(
                        Speech::getTargetLevel,
                        () -> new LinkedHashMap<>(),
                        Collectors.toList()
                ));

        // 3. 레벨별 ScorePoint 시리즈 생성
        List<GrowthHistoryResponse> result = new ArrayList<>();

        // TargetLevel enum 순서대로 정렬하여 반환
        Arrays.stream(TargetLevel.values()).forEach(level -> {
            List<Speech> levelSpeeches = grouped.get(level);
            if (levelSpeeches == null || levelSpeeches.isEmpty()) return;

            List<GrowthHistoryResponse.ScorePoint> points = new ArrayList<>();
            int index = 1;

            for (Speech speech : levelSpeeches) {
                Optional<SpeechAnalysis> analysis =
                        speechAnalysisRepository.findBySpeechSpeechId(speech.getSpeechId());
                if (analysis.isEmpty()) continue;

                double avg = analysis.get().calculateAverageScore();
                // 소수점 1자리 반올림
                avg = Math.round(avg * 10.0) / 10.0;

                points.add(GrowthHistoryResponse.ScorePoint.builder()
                        .index(index++)
                        .date(speech.getCreatedAt().format(DATE_FMT))
                        .averageScore(avg)
                        .build());
            }

            if (points.isEmpty()) return;

            result.add(GrowthHistoryResponse.builder()
                    .targetLevel(level)
                    .levelLabel(getLevelLabel(level))
                    .scores(points)
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
