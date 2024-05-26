package com.withus.withmebe.common.websocket.factory;

import static org.springframework.messaging.simp.stomp.StompCommand.CONNECT;
import static org.springframework.messaging.simp.stomp.StompCommand.SUBSCRIBE;

import com.withus.withmebe.chat.repository.ChatRoomRepository;
import com.withus.withmebe.common.websocket.handler.ConnectCommandHandler;
import com.withus.withmebe.common.websocket.handler.StompCommandHandler;
import com.withus.withmebe.common.websocket.handler.SubscribeCommandHandler;
import com.withus.withmebe.security.jwt.provider.TokenProvider;
import java.util.EnumMap;
import java.util.Map;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.stereotype.Component;

@Component
public class StompCommandHandlerFactory {
  private static final Map<StompCommand, StompCommandHandler> handlers = new EnumMap<>(StompCommand.class);

  public StompCommandHandlerFactory(TokenProvider tokenProvider, ChatRoomRepository chatRoomRepository) {
    handlers.put(CONNECT, new ConnectCommandHandler(tokenProvider));
    handlers.put(SUBSCRIBE, new SubscribeCommandHandler(chatRoomRepository));
  }

  public StompCommandHandler getHandler(StompCommand command) {
    return handlers.get(command);
  }
}