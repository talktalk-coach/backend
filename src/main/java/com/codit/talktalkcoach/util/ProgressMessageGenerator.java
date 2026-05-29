package com.codit.talktalkcoach.util;

import com.codit.talktalkcoach.domain.entity.SpeechAnalysis;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;
import java.util.Objects;

/**
 * 직전 5개 스피치 평균 대비 현재 스피치 성장 멘트 생성 유틸
 * GPT 없이 수치 비교로 생성 → 추가 비용/지연 없음
 */
public class ProgressMessageGenerator {

    // 항목별 한국어 이름
    private static final Map<String, String> SCORE_LABELS = new LinkedHashMap<>() {{
        put("accuracy",   "발음 정확도");
        put("fluency",    "말의 유창함");
        put("prosody",    "억양과 리듬");
        put("vocabulary", "어휘 다양성");
        put("logic",      "논리 구성");
        put("structure",  "발표 구조");
    }};

    /**
     * @param current  현재 스피치 분석 결과
     * @param prevList 직전 최대 5개 스피치 분석 결과 목록
     * @return 성장 멘트 문자열
     */
    public static String generate(SpeechAnalysis current, List<SpeechAnalysis> prevList) {
        if (prevList == null || prevList.isEmpty()) {
            return "첫 번째 스피치를 완료했습니다! 앞으로의 성장이 기대됩니다.";
        }

        // 직전 평균 계산
        Map<String, Double> prevAvg = calcAverage(prevList);
        Map<String, Double> currScores = toMap(current);

        // 가장 변화가 큰 항목 찾기
        String bestKey   = null;
        double bestDiff  = Double.NEGATIVE_INFINITY;
        String worstKey  = null;
        double worstDiff = Double.POSITIVE_INFINITY;

        for (String key : SCORE_LABELS.keySet()) {
            Double prev = prevAvg.get(key);
            Double curr = currScores.get(key);
            if (prev == null || curr == null || prev == 0) continue;

            double diff = curr - prev;
            if (diff > bestDiff)  { bestDiff  = diff;  bestKey  = key; }
            if (diff < worstDiff) { worstDiff = diff;  worstKey = key; }
        }

        // 전체 평균 변화
        double prevTotal = prevList.stream()
                .mapToDouble(SpeechAnalysis::calculateAverageScore)
                .average().orElse(0);
        double currTotal = current.calculateAverageScore();
        double totalDiff = currTotal - prevTotal;

        return buildMessage(bestKey, bestDiff, worstKey, worstDiff, totalDiff);
    }

    private static String buildMessage(String bestKey, double bestDiff,
                                       String worstKey, double worstDiff,
                                       double totalDiff) {
        StringBuilder msg = new StringBuilder();

        // 전체 평균 성장/하락 멘트
        if (totalDiff >= 5) {
            msg.append(String.format("지난 연습 대비 전체 점수가 %.0f점 향상되었습니다! ", totalDiff));
        } else if (totalDiff >= 1) {
            msg.append(String.format("지난 연습 대비 전체 점수가 %.0f점 올랐습니다. ", totalDiff));
        } else if (totalDiff <= -5) {
            msg.append(String.format("지난 연습 대비 전체 점수가 %.0f점 낮아졌습니다. 더 연습해봐요. ", Math.abs(totalDiff)));
        } else {
            msg.append("지난 연습과 비슷한 수준을 유지하고 있습니다. ");
        }

        // 가장 많이 성장한 항목
        if (bestKey != null && bestDiff > 0) {
            String label = SCORE_LABELS.get(bestKey);
            double pct = Math.abs(bestDiff);
            msg.append(String.format("특히 %s이(가) %.0f점 향상되었습니다.", label, pct));
        }

        // 가장 많이 하락한 항목 (3점 이상 하락 시에만)
        if (worstKey != null && worstDiff < -3 && !worstKey.equals(bestKey)) {
            String label = SCORE_LABELS.get(worstKey);
            msg.append(String.format(" %s 부분은 조금 더 신경써보세요.", label));
        }

        return msg.toString().trim();
    }

    // SpeechAnalysis → Map 변환
    private static Map<String, Double> toMap(SpeechAnalysis a) {
        Map<String, Double> map = new LinkedHashMap<>();
        map.put("accuracy",   a.getAccuracyScore());
        map.put("fluency",    a.getFluencyScore());
        map.put("prosody",    a.getProsodyScore());
        map.put("vocabulary", a.getVocabularyScore());
        map.put("logic",      a.getLogicScore());
        map.put("structure",  a.getStructureScore());
        return map;
    }

    // 여러 SpeechAnalysis → 항목별 평균
    private static Map<String, Double> calcAverage(List<SpeechAnalysis> list) {
        Map<String, Double> result = new LinkedHashMap<>();
        for (String key : SCORE_LABELS.keySet()) {
            double avg = list.stream()
                    .map(a -> getScore(a, key))
                    .filter(Objects::nonNull)
                    .mapToDouble(Double::doubleValue)
                    .average().orElse(0);
            result.put(key, avg);
        }
        return result;
    }

    private static Double getScore(SpeechAnalysis a, String key) {
        return switch (key) {
            case "accuracy"   -> a.getAccuracyScore();
            case "fluency"    -> a.getFluencyScore();
            case "prosody"    -> a.getProsodyScore();
            case "vocabulary" -> a.getVocabularyScore();
            case "logic"      -> a.getLogicScore();
            case "structure"  -> a.getStructureScore();
            default -> null;
        };
    }
}
