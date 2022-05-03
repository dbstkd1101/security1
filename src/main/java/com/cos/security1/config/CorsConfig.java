package com.cos.security1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter()
    {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); //Client 자바스크립트에서 요청 시(ex : axios.fetch) 응답할 때 내 서버가 응답 처리 할 수 있게 할지 여부 설정
        config.addAllowedOrigin("*"); // 모든 ip 요청에 대한 응답 허용
        config.addAllowedMethod("*"); // 모든 method(post, get, put, delete, patch) 요청에 대한 응답 허용
        config.addAllowedHeader("*"); // 모든 header에 응답 허용
        source.registerCorsConfiguration("api/**", config);
        return new CorsFilter(source);
    }
}
