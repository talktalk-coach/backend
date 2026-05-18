package com.codit.talktalkcoach.service;

import com.codit.talktalkcoach.domain.entity.Speech;
import com.codit.talktalkcoach.domain.entity.SpeechAnalysis;
import com.codit.talktalkcoach.domain.entity.User;
import com.codit.talktalkcoach.domain.enums.SpeechCategory;
import com.codit.talktalkcoach.dto.response.speech.SpeechResultResponse;
import com.codit.talktalkcoach.dto.response.speech.SpeechStatusResponse;
import com.codit.talktalkcoach.dto.response.user.SpeechListResponse;
import com.codit.talktalkcoach.exception.BusinessException;
import com.codit.talktalkcoach.exception.ErrorCode;
import com.codit.talktalkcoach.exception.custom.SpeechNotFoundException;
import com.codit.talktalkcoach.repository.SpeechAnalysisRepository;
import com.codit.talktalkcoach.repository.SpeechRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpeechService {

    private final SpeechRepository speechRepository;
    private final SpeechAnalysisRepository speechAnalysisRepository;
    private final SpeechAnalysisAsyncService speechAnalysisAsyncService;
    private final S3Service s3Service;
    private final com.fasterxml.jackson.databind.ObjectMapper objectMapper;

    // ─── 업로드 + 비동기 분석 시작 ───────────────────────────────────────
    @Transactional
    public Long uploadAndAnalyze(User user, String title,
                                 MultipartFile audioFile, int duration,
                                 SpeechCategory category) {
        byte[] audioBytes;
        try {
            audioBytes = audioFile.getBytes();
            log.info("오디오 파일 읽기 완료: {} bytes ({}KB)",
                    audioBytes.length, audioBytes.length / 1024);
            if (audioBytes.length == 0) {
                log.error("업로드된 파일이 비어있습니다.");
                throw new BusinessException(ErrorCode.AZURE_API_ERROR);
            }
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("오디오 파일 읽기 실패: {}", e.getMessage());
            throw new BusinessException(ErrorCode.AZURE_API_ERROR);
        }

        // S3에 오디오 업로드
        String audioUrl = s3Service.upload(audioFile, "audio");

        Speech speech = Speech.builder()
                .user(user)
                .title(title)
                .audioUrl(audioUrl)
                .duration(duration)
                .targetLevel(user.getTargetLevel())
                .category(category)
                .build();
        speechRepository.save(speech);

        speechAnalysisAsyncService.analyzeAsync(speech.getSpeechId(), audioBytes);
        return speech.getSpeechId();
    }

    // ─── 상태 조회 ───────────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public SpeechStatusResponse getStatus(Long speechId, Long userId) {
        // userId가 null이면 개발 모드 — speechId만으로 조회
        Speech speech = (userId != null)
                ? getSpeechOfUser(speechId, userId)
                : speechRepository.findById(speechId).orElseThrow(SpeechNotFoundException::new);
        return switch (speech.getStatus()) {
            case PROCESSING -> SpeechStatusResponse.processing(speechId);
            case COMPLETED  -> SpeechStatusResponse.completed(speechId);
            case FAILED     -> SpeechStatusResponse.failed(speechId);
        };
    }

    // ─── 결과 조회 ───────────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public SpeechResultResponse getResult(Long speechId, Long userId) {
        // userId가 null이면 개발 모드 — speechId만으로 조회
        Speech speech = (userId != null)
                ? getSpeechOfUser(speechId, userId)
                : speechRepository.findById(speechId).orElseThrow(SpeechNotFoundException::new);
        SpeechAnalysis analysis = speechAnalysisRepository.findBySpeechSpeechId(speechId)
                .orElseThrow(() -> new BusinessException(ErrorCode.SPEECH_ANALYSIS_NOT_FOUND));
        return SpeechResultResponse.of(speech, analysis);
    }

    // ─── 목록 조회 ───────────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public SpeechListResponse getMySpeeches(User user, int page, String sort) {
        Sort sortObj = switch (sort) {
            case "date_asc" -> Sort.by("createdAt").ascending();
            default         -> Sort.by("createdAt").descending();
        };

        Page<Speech> speechPage = speechRepository.findByUser(
                user, PageRequest.of(page, 10, sortObj));

        List<SpeechListResponse.SpeechSummaryDto> dtos = speechPage.getContent().stream()
                .map(s -> {
                    Double avg = speechAnalysisRepository.findBySpeechSpeechId(s.getSpeechId())
                            .map(SpeechAnalysis::calculateAverageScore)
                            .orElse(null);
                    return SpeechListResponse.SpeechSummaryDto.from(s, avg);
                })
                .collect(Collectors.toList());

        return SpeechListResponse.builder()
                .speeches(dtos)
                .totalCount((int) speechPage.getTotalElements())
                .currentPage(page)
                .totalPages(speechPage.getTotalPages())
                .build();
    }

    // ─── 삭제 ────────────────────────────────────────────────────────────
    @Transactional
    public void delete(Long speechId, Long userId) {
        Speech speech = getSpeechOfUser(speechId, userId);
        speechRepository.delete(speech);
    }

    // ─── 내부 유틸 ───────────────────────────────────────────────────────
    private Speech getSpeechOfUser(Long speechId, Long userId) {
        Speech speech = speechRepository.findById(speechId)
                .orElseThrow(SpeechNotFoundException::new);
        if (!speech.getUser().getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }
        return speech;
    }
}
