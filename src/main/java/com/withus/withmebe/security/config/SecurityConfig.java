package com.withus.withmebe.security.config;

import com.withus.withmebe.security.handler.OAuth2FailHandler;
import com.withus.withmebe.security.handler.OAuth2SuccessHandler;
import com.withus.withmebe.security.jwt.filter.JwtAuthenticationFilter;
import com.withus.withmebe.security.service.OAuth2UserService;
import java.util.Arrays;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthenticationFilter authenticationFilter;
  private final OAuth2UserService oAuth2UserService;
  private final OAuth2SuccessHandler oAuth2SuccessHandler;
  private final OAuth2FailHandler oAuth2FailHandler;
  @Value("${front.url}")
  private String frontUrl;

  @Value("${spring.security.origin.allow.url}")
  private String allowedOrigin;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http,
      CorsConfigurationSource corsConfigurationSource) throws Exception {
    http
        .formLogin(AbstractHttpConfigurer::disable)

        .httpBasic(AbstractHttpConfigurer::disable)

        .csrf(AbstractHttpConfigurer::disable)

        .cors(config -> config.configurationSource(corsConfigurationSource))

        .sessionManagement(sessionManagement ->
            sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

        .authorizeHttpRequests(authorizeRequests ->
            authorizeRequests
                .requestMatchers("/api/auth/signup"
                    , "/api/auth/signin"
                    , "/api/auth/signin/**"
                    , "/api/member/check/email"
                    , "/api/comment/list/*"
                    , "/api/search/**"
                    , "/api/participation/count"
                    , "/api/festival"
                    , "/api/notification/subscribe"
                    , "/ws"
                    ,"/api/health/check"
                ).permitAll()
                .requestMatchers(HttpMethod.GET, "/api/gathering/**")
                .permitAll()
                .anyRequest().authenticated())
        .addFilterBefore(this.authenticationFilter, UsernamePasswordAuthenticationFilter.class)

        .oauth2Login(oauth2Configurer ->
            oauth2Configurer
                .loginPage(frontUrl+"/login")
                .successHandler(oAuth2SuccessHandler)
                .failureHandler(oAuth2FailHandler)
                .userInfoEndpoint(userInfo ->
                    userInfo.userService(oAuth2UserService)));
    return http.build();
  }

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
}
