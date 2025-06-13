package com.auction.adauctionbackend.config;

import com.auction.adauctionbackend.jwt.JwtTokenProvider;
import com.auction.adauctionbackend.security.CustomUserDetailsService;
import com.auction.adauctionbackend.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    /**
     * Spring Security 필터 체인을 구성합니다.
     * CSRF 비활성화, 세션 관리 정책 설정, 요청 인가 규칙 정의 등이 포함됩니다.
     * 
     * @param http HttpSecurity 객체
     * @return 구성된 SecurityFilterChain 객체
     * @throws Exception 보안 구성 중 발생할 수 있는 예외
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // CSRF 보호 비활성화 (JWT 사용 시)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // JWT 사용
                                                                                                              // 시 세션 사용
                                                                                                              // 안함
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.OPTIONS, "/api/v1/**").permitAll() // CORS 사전 요청 (OPTIONS) 허용
                        .requestMatchers("/api/v1/users/register", "/api/v1/users/login").permitAll() // 회원가입 및 로그인 경로는
                                                                                                      // 모두 허용
                        .anyRequest().authenticated() // 그 외 모든 요청은 인증 필요
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class); // JWT 필터 추가

        return http.build();
    }

    /**
     * 비밀번호 인코더를 빈으로 등록합니다.
     * BCrypt 해싱 알고리즘을 사용합니다.
     * 
     * @return PasswordEncoder 구현체
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * AuthenticationManager를 빈으로 등록합니다.
     * 
     * @param authenticationConfiguration 인증 설정
     * @return AuthenticationManager 객체
     * @throws Exception 예외
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * DaoAuthenticationProvider를 구성합니다.
     * CustomUserDetailsService와 PasswordEncoder를 사용하여 인증을 처리합니다.
     * 
     * @return DaoAuthenticationProvider 객체
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /**
     * ProviderManager를 빈으로 등록하여 DaoAuthenticationProvider를 사용하도록 합니다.
     * 
     * @return ProviderManager 객체
     */
    @Bean
    public ProviderManager providerManager() {
        return new ProviderManager(daoAuthenticationProvider());
    }
}