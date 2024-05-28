package com.withus.withmebe.common.websocket.interceptor;

import static com.withus.withmebe.common.exception.ExceptionCode.STOMP_HEADER_ACCESSOR_NOT_FOUND_EXCEPTION;

import com.withus.withmebe.common.exception.CustomException;
import com.withus.withmebe.common.websocket.factory.StompCommandHandlerFactory;
import com.withus.withmebe.common.websocket.handler.StompCommandHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketInterceptor implements ChannelInterceptor {

  private final StompCommandHandlerFactory handlerFactory;

  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

    if (accessor == null) {
      log.info("[ConnectCommandHandler][preSend] accessor is null");
      throw new CustomException(STOMP_HEADER_ACCESSOR_NOT_FOUND_EXCEPTION);
    }

    StompCommandHandler handler = handlerFactory.getHandler(accessor.getCommand());

    if (handler != null) {
      handler.handle(accessor);
    }

    return message;
  }
}