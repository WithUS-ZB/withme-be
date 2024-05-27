package com.withus.withmebe.common.websocket.handler;

import static com.withus.withmebe.common.exception.ExceptionCode.AUTHENTICATION_ISSUE;
import static com.withus.withmebe.security.jwt.filter.JwtAuthenticationFilter.TOKEN_PREFIX;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.withus.withmebe.common.exception.CustomException;
import com.withus.withmebe.security.jwt.provider.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.util.ObjectUtils;

@Slf4j
@RequiredArgsConstructor
public class ConnectCommandHandler implements StompCommandHandler{
  private final TokenProvider tokenProvider;
  @Override
  public void handle(StompHeaderAccessor accessor) {
    String accessToken = resolveAccessTokenFromStompHeaderAccessor(accessor);
    log.info("[ConnectCommandHandler][handle]" + accessToken);

    if (!tokenProvider.validAccessToken(accessToken)) {
      throw new CustomException(AUTHENTICATION_ISSUE);
    }

    Authentication authentication = tokenProvider.getAuthentication(accessToken);
    accessor.setUser(authentication);
  }

  private String resolveAccessTokenFromStompHeaderAccessor(StompHeaderAccessor accessor) {
    String authorizationValue = accessor.getFirstNativeHeader(AUTHORIZATION);
    if (!ObjectUtils.isEmpty(authorizationValue) && authorizationValue.startsWith(TOKEN_PREFIX)) {
      return authorizationValue.substring(TOKEN_PREFIX.length());
    }
    return null;
  }
}
