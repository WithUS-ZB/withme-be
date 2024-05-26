package com.withus.withmebe.chat.service;

import static com.withus.withmebe.common.exception.ExceptionCode.AUTHORIZATION_ISSUE;
import static com.withus.withmebe.common.exception.ExceptionCode.ENTITY_NOT_FOUND;
import static com.withus.withmebe.participation.type.Status.CHAT_JOINED;

import com.withus.withmebe.chat.dto.ChatMessageDto;
import com.withus.withmebe.chat.dto.response.ChatRoomDto;
import com.withus.withmebe.chat.dto.response.ParticipationInfoOfChatroom;
import com.withus.withmebe.chat.entity.ChatRoom;
import com.withus.withmebe.chat.repository.ChatRoomRepository;
import com.withus.withmebe.common.exception.CustomException;
import com.withus.withmebe.gathering.entity.Gathering;
import com.withus.withmebe.gathering.repository.GatheringRepository;
import com.withus.withmebe.member.dto.member.MemberInfoDto;
import com.withus.withmebe.member.entity.Member;
import com.withus.withmebe.participation.repository.ParticipationRepository;
import com.withus.withmebe.participation.service.ParticipationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

  private final ChatRoomRepository chatRoomRepository;
  private final GatheringRepository gatheringRepository;
  private final ParticipationRepository participationRepository;

  private final ParticipationService participationService;
  private final ChatMessageService chatMessageService;

  @Transactional
  public ChatRoomDto create(Long currentMemberId, Long gatheringId) {
    Gathering gathering = readGatheringByIdOrThrow(gatheringId);
    if (!gathering.isHost(currentMemberId)) {
      throw new CustomException(AUTHORIZATION_ISSUE);
    }
    ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.builder().gathering(gathering).build());
    participationService.createParticipationByHost(currentMemberId, gathering.getId());

    return chatRoom.toDto();
  }

  @Transactional
  public ChatMessageDto join(Long memberId, Long chatroomId, Long participationId) {
    participationService.joinChatroom(memberId, participationId);
    // TODO: 동시성 문제 해결 필요
    readChatroomByIdOrThrow(chatroomId).memberCountUp();
    return chatMessageService.join(memberId, chatroomId);
  }

  @Transactional
  public ChatMessageDto leave(Long memberId, Long chatroomId, Long participationId) {
    participationService.leaveChatroom(memberId, participationId);
    // TODO: 동시성 문제 해결 필요
    readChatroomByIdOrThrow(chatroomId).memberCountDown();
    return chatMessageService.leave(memberId, chatroomId);
  }

  @Transactional(readOnly = true)
  public Page<ChatRoomDto> readMyList(Long currentMemberId, Pageable pageable) {
    return chatRoomRepository.findChatRoomsByStatusAndParticipantId(
        CHAT_JOINED, currentMemberId, pageable).map(ChatRoom::toDto);
  }

  @Transactional(readOnly = true)
  public List<MemberInfoDto> readParticipantsByRoom(Long memberId, Long roomId) {
    if (!chatRoomRepository.existsByCurrentMemberIdAndRoomIdAndParticipationStatus(memberId, roomId,
        CHAT_JOINED)) {
      throw new CustomException(AUTHORIZATION_ISSUE);
    }
    return chatRoomRepository.findByParticipantOfChatroomByStatusAndRoomId(CHAT_JOINED, roomId)
        .stream().map(Member::toMemberInfoDto).toList();
  }

  @Transactional(readOnly = true)
  public ParticipationInfoOfChatroom readParticipationJoinInfo(Long memberId, Long chatroomId) {
    ChatRoom chatRoom = readChatroomByIdOrThrow(chatroomId);
    return participationRepository.findByParticipant_IdAndGatheringAndStatus(
            memberId, chatRoom.getGathering(), CHAT_JOINED)
        .orElseThrow(() -> new CustomException(ENTITY_NOT_FOUND))
        .toParticipationInfoOfChatroom(chatroomId, chatRoom.getTitle());
  }

  private Gathering readGatheringByIdOrThrow(Long gatheringId) {
    return gatheringRepository.findById(gatheringId)
        .orElseThrow(() -> new CustomException(ENTITY_NOT_FOUND));
  }

  private ChatRoom readChatroomByIdOrThrow(Long chatroomId) {
    return chatRoomRepository.findById(chatroomId)
        .orElseThrow(() -> new CustomException(ENTITY_NOT_FOUND));
  }
}
