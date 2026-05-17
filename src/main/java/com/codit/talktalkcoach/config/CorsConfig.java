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

        //프론트 url 넣기
        config.setAllowedOriginPatterns(List.of("*"));  // 임시: 모든 Origin 허용

        // ── 허용할 HTTP 메서드 ──────────────────────────────────────────────
        config.setAllowedMethods(List.of(
                "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
        ));

        // ── 허용할 헤더 ────────────────────────────────────────────────────
        //config.setAllowedHeaders(List.of("*"));
        config.setAllowedHeaders(List.of("https://talktalkcoach.vercel.app/auth/landing"));

        // ── 인증 정보 포함 여부 (쿠키, Authorization 헤더 등) ───────────────
        // AllowedOriginPatterns("*") 와 함께 사용 가능
        config.setAllowCredentials(true);

        // ── preflight 캐시 시간 (초) ─────────────────────────────────────
        config.setMaxAge(3600L);

        // ── 응답 헤더 노출 (프론트에서 읽을 수 있도록) ──────────────────────
        config.setExposedHeaders(List.of(
                "Authorization",
                "Content-Disposition"
        ));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);  // 모든 경로에 적용

        return new CorsFilter(source);
    }
}
