package com.withus.withmebe.common.websocket.interceptor;

import static com.withus.withmebe.common.exception.ExceptionCode.AUTHENTICATION_ISSUE;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.withus.withmebe.common.exception.CustomException;
import com.withus.withmebe.security.jwt.provider.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketInterceptor implements ChannelInterceptor {

  public static final String TOKEN_PREFIX = "Bearer ";

  private final TokenProvider tokenProvider;

  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    StompHeaderAccessor accessor
        = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

    if (accessor != null && accessor.getCommand() == StompCommand.CONNECT) {
      String accessToken = resolveAccessTokenFromStompHeaderAccessor(accessor);

      if (!tokenProvider.validAccessToken(accessToken)) {
        throw new CustomException(AUTHENTICATION_ISSUE);
      }
      Authentication authentication = this.tokenProvider.getAuthentication(accessToken);
      accessor.setUser(authentication);
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    return message;
  }

  private String resolveAccessTokenFromStompHeaderAccessor(StompHeaderAccessor accessor) {
    if (accessor.getCommand() == StompCommand.CONNECT) {
      String authorizationValue = accessor.getFirstNativeHeader(AUTHORIZATION);
      if (!ObjectUtils.isEmpty(authorizationValue) && authorizationValue.startsWith(TOKEN_PREFIX)) {
        return authorizationValue.substring(TOKEN_PREFIX.length());
      }
    }
    return null;
  }
}
