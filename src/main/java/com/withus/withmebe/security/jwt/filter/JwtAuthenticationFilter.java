package com.withus.withmebe.security.jwt.filter;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.withus.withmebe.security.jwt.provider.TokenProvider;
import com.withus.withmebe.security.util.MySecurityUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  public static final String TOKEN_PREFIX = "Bearer ";

  private final TokenProvider tokenProvider;


  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String accessToken = this.resolveTokenFromRequest(request);
    log.info("[JwtAuthenticationFilter]" + accessToken);
    // 토큰 유효성 검증
    if (tokenProvider.validAccessToken(accessToken)
    ) {
      // 시큐리티 컨텍스트에 인증정보를 넣어줌
      SecurityContextHolder.getContext().setAuthentication(
          this.tokenProvider.getAuthentication(accessToken));

      log.info("[{}] -> {}",
          MySecurityUtil.getCustomUserDetails().getUsername(), request.getRequestURI());
    }
    // 필터가 연속적으로 되도록
    filterChain.doFilter(request, response);
  }

  private String resolveTokenFromRequest(HttpServletRequest request) {
    String token = request.getHeader(AUTHORIZATION);

    if (!ObjectUtils.isEmpty(token) && token.startsWith(TOKEN_PREFIX)) {
      return token.substring(TOKEN_PREFIX.length());
    }

    return null;
  }
}