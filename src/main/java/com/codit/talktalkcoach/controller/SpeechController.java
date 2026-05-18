package com.codit.talktalkcoach.controller;

import com.codit.talktalkcoach.domain.enums.SpeechCategory;
import com.codit.talktalkcoach.dto.response.speech.SpeechResultResponse;
import com.codit.talktalkcoach.dto.response.speech.SpeechStatusResponse;
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

        Long speechId = speechService.uploadAndAnalyze(
                userDetails.getUser(), title, audio, duration, category);
        return ResponseEntity.accepted().body(speechId);
    }

    @Operation(summary = "분석 상태 조회")
    @GetMapping("/status/{speechId}")
    public ResponseEntity<SpeechStatusResponse> getStatus(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long speechId) {
        return ResponseEntity.ok(speechService.getStatus(speechId, userDetails.getUserId()));
    }

    @Operation(summary = "분석 결과 조회")
    @GetMapping("/results/{speechId}")
    public ResponseEntity<SpeechResultResponse> getResult(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long speechId) {
        return ResponseEntity.ok(speechService.getResult(speechId, userDetails.getUserId()));
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
        speechService.delete(speechId, userDetails.getUserId());
        return ResponseEntity.noContent().build();
    }
}
