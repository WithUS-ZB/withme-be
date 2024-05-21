package com.withus.withmebe.chat.controller;

import com.withus.withmebe.chat.dto.response.ChatRoomDto;
import com.withus.withmebe.chat.service.ChatRoomService;
import com.withus.withmebe.common.exception.CustomException;
import com.withus.withmebe.common.exception.ExceptionCode;
import com.withus.withmebe.security.anotation.CurrentMemberId;
import com.withus.withmebe.security.anotation.CurrentUserIsMobileAuthenticatedMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chatroom")
public class ChatRoomController {

  private final ChatRoomService chatRoomService;

  @PostMapping
  public ResponseEntity<ChatRoomDto> add(
      @CurrentMemberId Long currentMemberId,
      @CurrentUserIsMobileAuthenticatedMember boolean isMobileAuthenticatedMember,
      @RequestParam Long gatheringId
  ) {
    if (!isMobileAuthenticatedMember) {
      throw new CustomException(ExceptionCode.AUTHENTICATION_ISSUE);
    }
    return ResponseEntity.ok(
        chatRoomService.create(currentMemberId, gatheringId)
    );
  }
}
