package com.withus.withmebe.chat.controller;

import com.withus.withmebe.chat.dto.ChatMessageDto;
import com.withus.withmebe.chat.dto.request.ChatMessageRequestDto;
import com.withus.withmebe.chat.service.ChatMessageService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatMessageController {
  private static final String DESTINATION_PREFIX = "/topic/chatroom/";
  private final ChatMessageService chatMessageService;
  private final SimpMessagingTemplate messagingTemplate;
  @MessageMapping("/message")
  public void message(Principal principal
      , @Payload ChatMessageRequestDto request) {
    ChatMessageDto chatMessageDto = chatMessageService.chat(
        Long.valueOf(principal.getName()), request);
    messagingTemplate.convertAndSend(DESTINATION_PREFIX + request.chatroomId()
        , chatMessageDto);
  }
}
