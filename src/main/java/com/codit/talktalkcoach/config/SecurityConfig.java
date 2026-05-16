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

    private static final String[] PUBLIC_URLS = {
            // ── 인증 API ──────────────────────────────────────────────────────
            "/api/auth/**",

            // ── 개발 테스트 전용 ───────────────────────────────────────────────
            "/api/test/**",

            // ── Swagger / SpringDoc ───────────────────────────────────────────
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/webjars/**",
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                    // ── 개발 중 전체 허용 ─────────────────────────────────────
                    // TODO: 운영 배포 전 아래 줄 제거 후 그 아래 주석 해제
                    .anyRequest().permitAll()

                    // ── 운영용 (위 줄 제거 후 아래 주석 해제) ───────────────────
                    // .requestMatchers(PUBLIC_URLS).permitAll()
                    // .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);

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
