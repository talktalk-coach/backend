package com.codit.talktalkcoach.controller;

import com.codit.talktalkcoach.dto.request.user.GradeUpdateRequest;
import com.codit.talktalkcoach.dto.request.user.NicknameUpdateRequest;
import com.codit.talktalkcoach.dto.response.user.GrowthHistoryResponse;
import com.codit.talktalkcoach.dto.response.user.SpeechListResponse;
import com.codit.talktalkcoach.dto.response.user.UserInfoResponse;
import com.codit.talktalkcoach.dto.response.user.UserStatisticsResponse;
import com.codit.talktalkcoach.security.CustomUserDetails;
import com.codit.talktalkcoach.service.GrowthHistoryService;
import com.codit.talktalkcoach.service.S3Service;
import com.codit.talktalkcoach.service.SpeechService;
import com.codit.talktalkcoach.service.UserService;
import com.codit.talktalkcoach.service.UserStatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Tag(name = "유저", description = "내 정보 조회/수정, 학습 통계")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "BearerAuth")
public class UserController {

    private final UserService userService;
    private final UserStatisticsService userStatisticsService;
    private final GrowthHistoryService growthHistoryService;
    private final SpeechService speechService;
    private final S3Service s3Service;

    @Operation(summary = "내 정보 조회")
    @GetMapping("/me")
    public ResponseEntity<UserInfoResponse> getMyInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails != null ? userDetails.getUserId() : null;
        return ResponseEntity.ok(userService.getMyInfo(userId));
    }

    @Operation(summary = "닉네임 수정")
    @PatchMapping("/me/nickname")
    public ResponseEntity<Void> updateNickname(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody NicknameUpdateRequest request) {
        userService.updateNickname(userDetails.getUserId(), request.getNickname());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "프로필 이미지 업로드")
    @PostMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> uploadProfileImage(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam("image") MultipartFile image) {
        String imageUrl = s3Service.upload(image, "profile");
        userService.updateProfileImage(userDetails.getUserId(), imageUrl);
        return ResponseEntity.ok(Map.of("imageUrl", imageUrl));
    }

    @Operation(
            summary = "학습 단계(학년) 변경",
            description = "targetLevel: ELEM_1_2 | ELEM_3_4 | ELEM_5_6 | MIDDLE_1_2 | MIDDLE_3"
    )
    @PatchMapping("/me/grade")
    public ResponseEntity<Void> updateGrade(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody GradeUpdateRequest request) {
        userService.updateTargetLevel(userDetails.getUserId(), request.getTargetLevel());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "내 스피치 목록 조회")
    @GetMapping("/me/speeches")
    public ResponseEntity<SpeechListResponse> getMySpeeches(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "date_desc") String sort) {
        return ResponseEntity.ok(
                speechService.getMySpeeches(userDetails.getUser(), page, sort));
    }

    @Operation(
            summary = "학습 통계 조회",
            description = "성장률, 핵심역량 마스터리, 최근 7일 일별 점수"
    )
    @GetMapping("/me/statistics")
    public ResponseEntity<UserStatisticsResponse> getStatistics(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(
                userStatisticsService.getStatistics(userDetails.getUser()));
    }

    @Operation(
            summary = "성장치 히스토리 조회",
            description = """
                    학년(targetLevel)별 스피치 회차 순서대로 averageScore를 반환합니다.
                    
                    - COMPLETED 상태인 스피치만 포함
                    - 학년별로 분리된 시계열 데이터 (차트의 각 선 = 1개 targetLevel)
                    - 학년을 변경한 적 없으면 1개 시리즈만 반환
                    - 데이터 없는 학년은 응답에서 제외
                    
                    응답 예시:
                    [
                      {
                        "targetLevel": "ELEM_3_4",
                        "levelLabel": "초등 3~4학년",
                        "scores": [
                          { "index": 1, "date": "2026-03-01", "averageScore": 71.2 },
                          { "index": 2, "date": "2026-03-15", "averageScore": 74.8 }
                        ]
                      },
                      {
                        "targetLevel": "MIDDLE_1_2",
                        "levelLabel": "중학교 1~2학년",
                        "scores": [
                          { "index": 1, "date": "2026-04-20", "averageScore": 69.3 }
                        ]
                      }
                    ]
                    """
    )
    @GetMapping("/me/growth-history")
    public ResponseEntity<List<GrowthHistoryResponse>> getGrowthHistory(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(
                growthHistoryService.getGrowthHistory(userDetails.getUser()));
    }

    @Operation(summary = "회원 탈퇴 (soft delete)")
    @DeleteMapping("/me")
    public ResponseEntity<Void> withdraw(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        userService.withdraw(userDetails.getUserId());
        return ResponseEntity.noContent().build();
    }
}
