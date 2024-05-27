package com.withus.withmebe.common.websocket.handler;

import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

public interface StompCommandHandler {
  void handle(StompHeaderAccessor accessor);
}
