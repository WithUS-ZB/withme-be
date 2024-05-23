package com.withus.withmebe.chat.controller;

import com.withus.withmebe.chat.dto.ChatMessageDto;
import com.withus.withmebe.chat.service.ChatMessageService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatMessageController {
  private static final String DESTINATION_PREFIX = "/topic/chatroom/";
  private final ChatMessageService chatMessageService;
  private final SimpMessagingTemplate messagingTemplate;

  @MessageMapping("/room/{room_id}/join")
  public void join(Principal principal, @DestinationVariable("room_id") Long roomId) {
    ChatMessageDto chatMessageDto = chatMessageService.join(Long.valueOf(principal.getName()), roomId);
    messagingTemplate.convertAndSend(DESTINATION_PREFIX + roomId, chatMessageDto);
  }
}
