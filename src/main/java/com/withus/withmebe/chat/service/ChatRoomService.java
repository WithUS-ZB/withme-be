package com.withus.withmebe.chat.service;

import static com.withus.withmebe.common.exception.ExceptionCode.AUTHORIZATION_ISSUE;
import static com.withus.withmebe.common.exception.ExceptionCode.ENTITY_NOT_FOUND;

import com.withus.withmebe.chat.dto.response.ChatRoomDto;
import com.withus.withmebe.chat.entity.ChatRoom;
import com.withus.withmebe.chat.repository.ChatRoomRepository;
import com.withus.withmebe.common.exception.CustomException;
import com.withus.withmebe.gathering.entity.Gathering;
import com.withus.withmebe.gathering.repository.GatheringRepository;
import com.withus.withmebe.participation.service.ParticipationService;
import com.withus.withmebe.participation.type.Status;
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
  private final ParticipationService participationService;

  @Transactional
  public ChatRoomDto create(Long currentMemberId, Long gatheringId) {
    Gathering gathering = getGatheringById(gatheringId);
    if (!gathering.isHost(currentMemberId)) {
      throw new CustomException(AUTHORIZATION_ISSUE);
    }
    ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.builder().gathering(gathering).build());
    participationService.createParticipationByHost(currentMemberId, gathering);

    return chatRoom.toDto();
  }

  @Transactional(readOnly = true)
  public Page<ChatRoomDto> readMyList(Long currentMemberId, Pageable pageable) {
    return chatRoomRepository.findChatRoomsByStatusAndParticipantId(
        Status.CHAT_JOINED, currentMemberId, pageable).map(ChatRoom::toDto);
  }

  private Gathering getGatheringById(Long gatheringId) {
    return gatheringRepository.findById(gatheringId)
        .orElseThrow(() -> new CustomException(ENTITY_NOT_FOUND));
  }
}
