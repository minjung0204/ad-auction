package com.auction.adauctionbackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/v1/**") // /api/v1 경로로 오는 모든 요청에 대해 CORS 설정 적용
                .allowedOrigins("http://localhost:3000") // 허용할 Origin (프론트엔드 URL)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 허용할 HTTP 메서드
                .allowedHeaders("*") // 모든 헤더 허용
                .allowCredentials(true) // 자격 증명 (쿠키, HTTP 인증 등) 허용
                .maxAge(3600); // 사전 요청 (pre-flight) 캐시 시간 (초)
    }
}