package com.codit.talktalkcoach.repository;

import com.codit.talktalkcoach.domain.entity.SpeechAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpeechAnalysisRepository extends JpaRepository<SpeechAnalysis, Long> {
    Optional<SpeechAnalysis> findBySpeechSpeechId(Long speechId);
}
