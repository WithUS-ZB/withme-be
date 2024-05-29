## 1. 문제 상황
 - 제작한 사이트에서 배포된 API 서버로 HTTP 요청을 보냈으나 CORS 정책에 의해 응답이 오지 않고 있는 상황

## 2. 원인
 - Spring Security의 CORS 정책을 설정하지 않음

## 3. 해결 방안
 - Spring Security 설정에 CORS 정책 추가

```

@Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();

    config.setAllowCredentials(true);
    config.setAllowedOriginPatterns(Collections.singletonList(allowedOrigin));
    config.setAllowedMethods(Arrays.asList("HEAD", "POST", "GET", "DELETE", "PUT"));
    config.setAllowedHeaders(Collections.singletonList("*"));
    config.setExposedHeaders(Collections.singletonList("Authorization"));

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
  }

```