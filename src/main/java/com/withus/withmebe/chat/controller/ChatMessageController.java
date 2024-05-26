package com.withus.withmebe.chat.controller;

import static org.springframework.data.domain.Sort.Direction.DESC;

import com.withus.withmebe.chat.dto.ChatMessageDto;
import com.withus.withmebe.chat.dto.request.ChatMessageRequestDto;
import com.withus.withmebe.chat.service.ChatMessageService;
import com.withus.withmebe.security.anotation.CurrentMemberId;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat-message")
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

  @GetMapping("/list")
  public ResponseEntity<Page<ChatMessageDto>> getListByRoomId(
      @CurrentMemberId Long currentMemberId
      , @RequestParam("roomid") Long roomId
      , @PageableDefault(value = 20, sort = "createdDttm", direction = DESC) Pageable pageable
  ) {
    return ResponseEntity.ok(chatMessageService.readListByRoomId(currentMemberId, roomId, pageable));
  }
}
