package com.withus.withmebe.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/api/auth/**")
        .allowCredentials(true)
        .allowedOriginPatterns("*")
        .exposedHeaders("Authorization"); // 노출할 사용자 지정 응답 헤더 설정

    registry.addMapping("/login/**")
        .allowCredentials(true)
        .allowedOriginPatterns("*")
        .exposedHeaders("Authorization");
  }
}
