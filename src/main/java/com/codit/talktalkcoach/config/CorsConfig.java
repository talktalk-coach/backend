package com.codit.talktalkcoach.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // ── 허용할 Origin ───────────────────────────────────────────────────
        config.setAllowedOrigins(List.of(
                "http://localhost:3000",    // 프론트 로컬 개발 서버 (Next.js 기본 포트)
                "http://127.0.0.1:3000"    // 동일한 로컬 환경 (IP 방식)
                // TODO: 운영 도메인 확정 시 아래 추가
                // "https://talktalkcoach.com"
        ));

        // ── 허용할 HTTP 메서드 ──────────────────────────────────────────────
        config.setAllowedMethods(List.of(
                "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
        ));

        // ── 허용할 헤더 ────────────────────────────────────────────────────
        config.setAllowedHeaders(List.of("*"));

        // ── 인증 정보 포함 여부 (Authorization 헤더, 쿠키 등) ───────────────
        config.setAllowCredentials(true);

        // ── preflight 캐시 시간 (초) ─────────────────────────────────────
        config.setMaxAge(3600L);

        // ── 응답 헤더 노출 (프론트에서 읽을 수 있도록) ──────────────────────
        config.setExposedHeaders(List.of(
                "Authorization",
                "Content-Disposition"
        ));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
