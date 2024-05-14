package com.withus.withmebe.security.config;

import com.withus.withmebe.security.handler.OAuth2SuccessHandler;
import com.withus.withmebe.security.jwt.filter.JwtAuthenticationFilter;
import com.withus.withmebe.security.service.OAuth2UserService;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http,
      CorsConfigurationSource corsConfigurationSource) throws Exception {
    http
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
                ).permitAll()
                .anyRequest().authenticated())
        .addFilterBefore(this.authenticationFilter, UsernamePasswordAuthenticationFilter.class)

        .oauth2Login(oauth2Configurer ->
            oauth2Configurer
                .loginPage("/api/auth/signin/oauth2")
                .successHandler(oAuth2SuccessHandler)
                .userInfoEndpoint()
                .userService(oAuth2UserService));
    return http.build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();

    config.setAllowCredentials(true);
    config.setAllowedOriginPatterns(Arrays.asList("*"));
    config.setAllowedMethods(Arrays.asList("HEAD", "POST", "GET", "DELETE", "PUT"));
    config.setAllowedHeaders(Arrays.asList("*"));
    config.setExposedHeaders(Arrays.asList("Authorization"));

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
  }
}
