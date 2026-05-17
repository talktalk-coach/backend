package com.codit.talktalkcoach.controller;

import com.codit.talktalkcoach.domain.entity.User;
import com.codit.talktalkcoach.domain.enums.SpeechCategory;
import com.codit.talktalkcoach.dto.response.speech.SpeechResultResponse;
import com.codit.talktalkcoach.dto.response.speech.SpeechStatusResponse;
import com.codit.talktalkcoach.repository.UserRepository;
import com.codit.talktalkcoach.security.CustomUserDetails;
import com.codit.talktalkcoach.service.SpeechService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "스피치 서비스", description = "스피치 녹음 업로드 및 분석 결과 조회")
@RestController
@RequestMapping("/api/speech")
@RequiredArgsConstructor
@SecurityRequirement(name = "BearerAuth")
public class SpeechController {

    private final SpeechService speechService;

    // [임시] 인증 없이 테스트할 때 기본 유저 조회용
    // TODO: 운영 전 제거
    private final UserRepository userRepository;
    private static final Long DEFAULT_TEST_USER_ID = 1L;

    @Operation(summary = "스피치 분석 요청")
    @PostMapping(value = "/azure", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> uploadAndAnalyze(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Parameter(description = "WAV 오디오 파일")
            @RequestPart("audio") MultipartFile audio,
            @Parameter(description = "스피치 제목")
            @RequestParam("title") String title,
            @Parameter(description = "녹음 시간 (초 단위)")
            @RequestParam("duration") int duration,
            @Parameter(description = "스피치 유형 (PRESENTATION | SPEECH | DEBATE)")
            @RequestParam(value = "category", defaultValue = "PRESENTATION") SpeechCategory category) {

        // [임시] 토큰 없으면 기본 테스트 유저 사용
        // TODO: 운영 전 → User user = userDetails.getUser(); 로 교체 후 헬퍼 메서드 제거
        User user = resolveUser(userDetails);
        Long speechId = speechService.uploadAndAnalyze(user, title, audio, duration, category);
        return ResponseEntity.accepted().body(speechId);
    }

    @Operation(summary = "분석 상태 조회")
    @GetMapping("/status/{speechId}")
    public ResponseEntity<SpeechStatusResponse> getStatus(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long speechId) {
        // userId null 허용 (SpeechService 내부에서 처리)
        Long userId = (userDetails != null) ? userDetails.getUserId() : null;
        return ResponseEntity.ok(speechService.getStatus(speechId, userId));
    }

    @Operation(summary = "분석 결과 조회")
    @GetMapping("/results/{speechId}")
    public ResponseEntity<SpeechResultResponse> getResult(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long speechId) {
        // [임시] 토큰 없으면 userId=null → SpeechService에서 speechId만으로 조회
        Long userId = (userDetails != null) ? userDetails.getUserId() : null;
        return ResponseEntity.ok(speechService.getResult(speechId, userId));
    }

    @Operation(summary = "결과 공유 URL 반환")
    @GetMapping("/results/{speechId}/share")
    public ResponseEntity<String> getShareUrl(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long speechId) {
        return ResponseEntity.ok("https://talktalkcoach.com/share/" + speechId);
    }

    @Operation(summary = "스피치 삭제 (soft delete)")
    @DeleteMapping("/{speechId}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long speechId) {
        Long userId = (userDetails != null) ? userDetails.getUserId() : null;
        speechService.delete(speechId, userId);
        return ResponseEntity.noContent().build();
    }

    // ─── [임시] 인증 없는 테스트용 유저 해석 헬퍼 ──────────────────────────
    // TODO: 운영 전 이 메서드 전체 제거
    private User resolveUser(CustomUserDetails userDetails) {
        if (userDetails != null) return userDetails.getUser();
        return userRepository.findById(DEFAULT_TEST_USER_ID)
                .orElseThrow(() -> new RuntimeException(
                        "테스트용 기본 유저(ID=" + DEFAULT_TEST_USER_ID + ")가 없습니다. " +
                        "POST /api/test/quick-signup 으로 먼저 유저를 생성해주세요."));
    }
}
