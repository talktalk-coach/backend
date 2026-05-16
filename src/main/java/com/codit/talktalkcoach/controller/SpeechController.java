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

    @Operation(
            summary = "스피치 분석 요청",
            description = """
                    WAV 파일을 업로드하면 Azure STT + GPT 분석을 비동기로 시작합니다.
                    응답으로 speechId를 반환하며, 이후 /status/{speechId}로 진행 상황을 확인하세요.
                    
                    **오디오 파일 요구사항**: WAV PCM, 16kHz, 16bit, mono
                    
                    **category**:
                    - PRESENTATION : 발표형 (기본값, 모든 레벨 허용)
                    - SPEECH       : 설득·연설형 (ELEM_5_6 이상)
                    - DEBATE       : 토론·논증형 (ELEM_5_6 이상)
                    
                    초1~2, 초3~4 레벨은 PRESENTATION 으로 자동 보정됩니다.
                    """
    )
    @PostMapping(value = "/azure", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> uploadAndAnalyze(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Parameter(description = "WAV 오디오 파일 (PCM 16kHz 16bit mono)")
            @RequestPart("audio") MultipartFile audio,
            @Parameter(description = "스피치 제목")
            @RequestParam("title") String title,
            @Parameter(description = "녹음 시간 (초 단위)")
            @RequestParam("duration") int duration,
            @Parameter(description = "스피치 유형 (PRESENTATION | SPEECH | DEBATE), 기본값: PRESENTATION")
            @RequestParam(value = "category", defaultValue = "PRESENTATION") SpeechCategory category) {

        Long speechId = speechService.uploadAndAnalyze(
                userDetails.getUser(), title, audio, duration, category);
        return ResponseEntity.accepted().body(speechId);
    }

    @Operation(summary = "분석 상태 조회", description = "PROCESSING / COMPLETED / FAILED 중 하나를 반환합니다.")
    @GetMapping("/status/{speechId}")
    public ResponseEntity<SpeechStatusResponse> getStatus(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long speechId) {
        // 개발 모드: userDetails가 null이면 userId 없이 speechId만으로 조회
        Long userId = (userDetails != null) ? userDetails.getUserId() : null;
        return ResponseEntity.ok(speechService.getStatus(speechId, userId));
    }

    @Operation(summary = "분석 결과 조회", description = "8개 항목 점수 + GPT 피드백 + STT 전사 텍스트를 반환합니다.")
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
