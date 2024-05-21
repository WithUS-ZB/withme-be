package com.withus.withmebe.chat.controller;


import com.withus.withmebe.chat.dto.response.ChatRoomDto;
import com.withus.withmebe.chat.service.ChatRoomService;
import com.withus.withmebe.participation.service.ParticipationService;
import com.withus.withmebe.security.anotation.CurrentMemberId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chatroom")
public class ChatRoomController {

  private final ChatRoomService chatRoomService;
  private final ParticipationService participationService;

  @PostMapping
  public ResponseEntity<ChatRoomDto> add(
      @CurrentMemberId Long currentMemberId,
      @RequestParam Long gatheringId
  ) {
    return ResponseEntity.ok(
        chatRoomService.create(currentMemberId, gatheringId)
    );
  }

  /**
   * 채팅방 참여
   *
   * @param currentMemberId 로그인 멤버 id
   * @param participationId 참여 id
   * @return 성공하면 200
   */
  @PutMapping
  public ResponseEntity<Void> join(
      @CurrentMemberId Long currentMemberId,
      @RequestParam Long participationId
  ) {
    participationService.joinChat(currentMemberId, participationId);
    return ResponseEntity.ok().build();
  }

  /**
   * 채팅방 나가기
   * @param currentMemberId 로그인 멤버 id
   * @param participationId 참여 id
   * @return 성공하면 200
   */
  @PutMapping
  public ResponseEntity<Void> leave(
      @CurrentMemberId Long currentMemberId,
      @RequestParam Long participationId
  ) {
    participationService.leaveChat(currentMemberId, participationId);
    return ResponseEntity.ok().build();
  }
}
