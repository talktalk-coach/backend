package com.codit.talktalkcoach.config;

import com.codit.talktalkcoach.repository.UserRepository;
import com.codit.talktalkcoach.security.jwt.JwtFilter;
import com.codit.talktalkcoach.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    // ── 운영용 PUBLIC_URLS (인증 복구 시 아래 주석 해제 후 사용) ──────────────
    // private static final String[] PUBLIC_URLS = {
    //         "/api/auth/**",
    //         "/api/test/**",
    //         "/swagger-ui.html",
    //         "/swagger-ui/**",
    //         "/v3/api-docs",
    //         "/v3/api-docs/**",
    //         "/swagger-resources/**",
    //         "/webjars/**",
    // };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                    // ══════════════════════════════════════════════════════
                    // [임시] 프론트 테스트용 — 모든 요청 인증 없이 허용
                    // TODO: 운영 전 아래 줄 제거하고 그 아래 블록 주석 해제
                    .anyRequest().permitAll()
                    // ══════════════════════════════════════════════════════
                    // [운영용] 위 줄 제거 후 아래 두 줄 주석 해제
                    // .requestMatchers(PUBLIC_URLS).permitAll()
                    // .anyRequest().authenticated()
                    // ══════════════════════════════════════════════════════
            )
            // ── [임시] JwtFilter 비활성화 — 토큰 없이도 API 호출 가능 ────────
            // TODO: 운영 전 아래 주석 해제
            // .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class)
            ;

        return http.build();
    }

    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter(jwtProvider, userRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
