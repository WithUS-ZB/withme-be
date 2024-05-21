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

  /**
   * 채팅방 개설
   * <p>
   * 채팅방이 개설되면 개설한 주최자 본인은 채팅방에 자동 참여 됩니다.
   * <p>
   * 모임의 주최자만 가능합니다.
   *
   * @param currentMemberId 로그인 멤버 id
   * @param gatheringId     채팅방을 개설할 모임
   * @return 성공하면 200과 ChatRoomDto 리턴
   */
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
  @PutMapping("/join")
  public ResponseEntity<Void> join(
      @CurrentMemberId Long currentMemberId,
      @RequestParam Long participationId
  ) {
    participationService.joinChat(currentMemberId, participationId);
    return ResponseEntity.ok().build();
  }

  /**
   * 채팅방 나가기
   *
   * @param currentMemberId 로그인 멤버 id
   * @param participationId 참여 id
   * @return 성공하면 200
   */
  @PutMapping("/leave")
  public ResponseEntity<Void> leave(
      @CurrentMemberId Long currentMemberId,
      @RequestParam Long participationId
  ) {
    participationService.leaveChat(currentMemberId, participationId);
    return ResponseEntity.ok().build();
  }
}
