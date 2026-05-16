package com.codit.talktalkcoach.controller;

import com.codit.talktalkcoach.domain.enums.TargetLevel;
import com.codit.talktalkcoach.dto.request.quiz.QuizSubmitRequest;
import com.codit.talktalkcoach.dto.response.quiz.DailyQuizResponse;
import com.codit.talktalkcoach.security.CustomUserDetails;
import com.codit.talktalkcoach.service.DailyQuizSchedulerService;
import com.codit.talktalkcoach.service.DailyQuizService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "데일리 퀴즈", description = "오늘의 어휘 퀴즈")
@RestController
@RequestMapping("/api/quiz")
@RequiredArgsConstructor
@SecurityRequirement(name = "BearerAuth")
public class QuizController {

    private final DailyQuizService dailyQuizService;
    private final DailyQuizSchedulerService schedulerService;

    @Operation(
            summary = "오늘의 퀴즈 3개 조회",
            description = """
                    유저의 학습 단계(targetLevel)에 맞는 오늘의 어휘 퀴즈 3개를 반환합니다.
                    
                    - 오늘 퀴즈가 없으면 즉시 GPT로 생성 후 반환
                    - 이미 제출한 퀴즈는 answered=true, isCorrect 포함
                    - 선택지는 매 호출마다 무작위로 섞임
                    """
    )
    @GetMapping("/today")
    public ResponseEntity<List<DailyQuizResponse>> getTodayQuiz(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(dailyQuizService.getTodayQuiz(userDetails.getUser()));
    }

    @Operation(
            summary = "퀴즈 정답 제출",
            description = """
                    선택한 선택지를 제출하고 정오답 결과를 반환합니다.
                    
                    응답:
                    - isCorrect: 정답 여부
                    - correctAnswer: 정답 텍스트
                    - todayCorrectCount: 오늘 맞춘 개수
                    - streak: "2/3" 형태 스트릭
                    """
    )
    @PostMapping("/submit")
    public ResponseEntity<Map<String, Object>> submitAnswer(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody QuizSubmitRequest request) {
        return ResponseEntity.ok(dailyQuizService.submitAnswer(userDetails.getUser(), request));
    }

    @Operation(
            summary = "[테스트] 퀴즈 즉시 생성",
            description = "스케줄러를 수동으로 실행하여 특정 레벨의 퀴즈를 즉시 생성합니다."
    )
    @PostMapping("/generate")
    public ResponseEntity<String> generateQuiz(
            @RequestParam(defaultValue = "MIDDLE_1_2") TargetLevel level) {
        try {
            schedulerService.generateQuizForLevel(level);
            return ResponseEntity.ok("퀴즈 생성 완료: " + level);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("생성 실패: " + e.getMessage());
        }
    }
}
