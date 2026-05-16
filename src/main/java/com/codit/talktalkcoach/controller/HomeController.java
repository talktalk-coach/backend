package com.codit.talktalkcoach.controller;

import com.codit.talktalkcoach.domain.entity.User;
import com.codit.talktalkcoach.dto.response.home.HomeResponse;
import com.codit.talktalkcoach.security.CustomUserDetails;
import com.codit.talktalkcoach.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "홈", description = "홈 화면 데이터")
@RestController
@RequestMapping("/api/home")
@RequiredArgsConstructor
@SecurityRequirement(name = "BearerAuth")
public class HomeController {

    private final DashboardService dashboardService;

    @Operation(
            summary = "홈 화면 통합 데이터",
            description = """
                    - **radarAverage**: 최근 5개 스피치 지표별 평균 (레이더 차트)
                    - **monthlyScores**: 이번 달 스피치 전체 평균 및 횟수
                    - **todayPracticeMinutes**: 이번 달 누적 스피치 시간 (분)
                    - **summaryFeedback**: GPT AI 통합 피드백 (스피치 완료 시 갱신)
                    - **totalCount**: 이번 달 스피치 총 횟수
                    """
    )
    @GetMapping
    public ResponseEntity<HomeResponse> getHome(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails != null ? userDetails.getUser() : null;
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).<HomeResponse>build();
        return ResponseEntity.ok(dashboardService.getHome(user));
    }

    @Operation(
            summary = "AI 피드백 강제 갱신",
            description = "summaryFeedback이 null일 때 수동으로 재생성합니다. (기존 스피치가 있어야 함)"
    )
    @PostMapping("/refresh-feedback")
    public ResponseEntity<Void> refreshFeedback(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        //문제발생.. userDetails가 null일 경우 NullPointerException 발생
        if (userDetails == null || userDetails.getUser() == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        dashboardService.refreshStats(userDetails.getUser());
        return ResponseEntity.ok().build();
    }
}
