package com.codit.talktalkcoach.config;

import com.codit.talktalkcoach.repository.UserRepository;
import com.codit.talktalkcoach.security.jwt.JwtFilter;
import com.codit.talktalkcoach.security.jwt.JwtProvider;
import com.codit.talktalkcoach.security.oauth2.CustomOAuth2UserService;
import com.codit.talktalkcoach.security.oauth2.OAuth2SuccessHandler;
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
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    private static final String[] PUBLIC_URLS = {
            // ── 인증 API ──────────────────────────────────────────────────────
            "/api/auth/**",
            // ── 소셔 로그인 ─────────────────────────────────────
            "/oauth2/**",
            "/login/oauth2/**",
            // ── 개발 테스트 전용 ───────────────────────────────────────────────
            "/api/test/**",
            // ── 퀴즈 생성 (스케줄러 수동 실행) ───────────────────────────────
            "/api/quiz/generate",
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
                    // OPTIONS preflight 요청은 인증 없이 전체 허용 (CORS 필수)
                    .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
                    .requestMatchers("/health").permitAll() // Health Check용 경로는 누구나 접근 가능                 .anyRequest().authenticated()
                    .requestMatchers(PUBLIC_URLS).permitAll()
                    .anyRequest().authenticated()
            )
            .exceptionHandling(ex -> ex
                    // 인증 실패 시 OAuth2 로그인 페이지 대신 401 반환
                    .authenticationEntryPoint((request, response, authException) -> {
                        response.setStatus(jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED);
                        response.setContentType("application/json;charset=UTF-8");
                        response.getWriter().write(
                                "{\"status\":401,\"code\":\"UNAUTHORIZED\",\"message\":\"\uc778증이 필요합니다.\"}"
                        );
                    })
            )
            .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class)
            .oauth2Login(oauth2 -> oauth2
                    // 소셔 로그인 시작 엔드포인트
                    // 프론트: GET /oauth2/authorize/google 또는 /oauth2/authorize/kakao
                    .authorizationEndpoint(ep -> ep
                            .baseUri("/oauth2/authorize"))
                    // 카카오/구글에서 콜백 리다이렉트 URI
                    .redirectionEndpoint(ep -> ep
                            .baseUri("/login/oauth2/code/*"))
                    // 유저 정보 조회 + 신규 회원 자동 가입
                    .userInfoEndpoint(ep -> ep
                            .userService(customOAuth2UserService))
                    // 로그인 성공 시 JWT 발급 후 프론트로 리다이렉트
                    .successHandler(oAuth2SuccessHandler)
            );

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
