package com.withus.withmebe.chat.controller;


import static org.springframework.data.domain.Sort.Direction.DESC;

import com.withus.withmebe.chat.dto.ChatMessageDto;
import com.withus.withmebe.chat.dto.response.ChatRoomDto;
import com.withus.withmebe.chat.dto.response.ParticipationInfoOfChatroom;
import com.withus.withmebe.chat.service.ChatRoomService;
import com.withus.withmebe.member.dto.member.MemberInfoDto;
import com.withus.withmebe.security.anotation.CurrentMemberId;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chatroom")
public class ChatRoomController {

  private static final String DESTINATION_PREFIX = "/topic/chatroom/";
  private final ChatRoomService chatRoomService;
  private final SimpMessagingTemplate messagingTemplate;

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
      @RequestParam("gatheringid") Long gatheringId
  ) {
    return ResponseEntity.ok(
        chatRoomService.create(currentMemberId, gatheringId)
    );
  }

  /**
   * 채팅방 참여
   *
   * @param currentMemberId 로그인 멤버 id
   * @param chatroomId      채팅방 id
   * @param participationId 참여 id
   * @return 성공하면 200
   */
  @PutMapping("/{room_id}/join")
  public ResponseEntity<Void> join(
      @CurrentMemberId Long currentMemberId
      , @PathVariable("room_id") Long chatroomId
      , @RequestParam("participationid") Long participationId
  ) {
    ChatMessageDto chatMessageDto = chatRoomService.join(currentMemberId, chatroomId,
        participationId);
    messagingTemplate.convertAndSend(DESTINATION_PREFIX + chatroomId, chatMessageDto);
    return ResponseEntity.ok().build();
  }

  /**
   * 채팅방 나가기
   *
   * @param currentMemberId 로그인 멤버 id
   * @param chatroomId      채팅방 id
   * @param participationId 참여 id
   * @return 성공하면 200
   */
  @PutMapping("/{room_id}/leave")
  public ResponseEntity<Void> leave(
      @CurrentMemberId Long currentMemberId
      , @PathVariable("room_id") Long chatroomId
      , @RequestParam("participationid") Long participationId
  ) {
    ChatMessageDto chatMessageDto = chatRoomService.leave(currentMemberId, chatroomId,
        participationId);
    messagingTemplate.convertAndSend(DESTINATION_PREFIX + chatroomId, chatMessageDto);
    return ResponseEntity.ok().build();
  }

  /**
   * 내가 참여하고 있는 채팅방 리스트 조회
   *
   * @param currentMemberId 로그인 멤버 id
   * @param pageable        페이지 설정
   * @return 성공시 200, 나의 참여 채팅방 목록
   */
  @GetMapping("/my-list")
  public ResponseEntity<Page<ChatRoomDto>> getMyList(
      @CurrentMemberId Long currentMemberId
      , @PageableDefault(value = 5, sort = "lastMessageDttm", direction = DESC) Pageable pageable) {
    return ResponseEntity.ok(
        chatRoomService.readMyList(currentMemberId, pageable)
    );
  }

  /**
   * 방에 참여하고 있는 참여자 리스트 조회
   *
   * @param currentMemberId 로그인 멤버 id
   * @param roomId          채팅방 id
   * @return 성공하면 200, 멤버 info
   */
  @GetMapping("/{room_id}/participants")
  public ResponseEntity<List<MemberInfoDto>> getParticipantsByRoom(
      @CurrentMemberId Long currentMemberId
      , @PathVariable("room_id") Long roomId) {
    return ResponseEntity.ok(
        chatRoomService.readParticipantsByRoom(currentMemberId, roomId)
    );
  }

  /**
   * 참여중인 채팅방에 대한 나의 참여 정보 조회
   * <p>
   * 해당 채팅룸에 대한 참여 정보를 가져옵니다.
   *
   * @param currentMemberId 로그인 멤버 id
   * @param chatroomId      채팅방 id
   * @return 성공하면 200과 참가 정보를 반환합니다. / 참여중이 아니면 404 Error 를 반환합니다.
   */
  @GetMapping("/{room_id}/my-participation-join-info")
  public ResponseEntity<ParticipationInfoOfChatroom> getMyParticipationJoinInfoOfChatroom(
      @CurrentMemberId Long currentMemberId
      , @PathVariable("room_id") Long chatroomId
  ) {
    return ResponseEntity.ok(
        chatRoomService.readParticipationJoinInfo(currentMemberId, chatroomId)
    );
  }
}
