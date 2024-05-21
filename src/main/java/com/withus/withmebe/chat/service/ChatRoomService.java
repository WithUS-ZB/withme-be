package com.withus.withmebe.chat.service;

import static com.withus.withmebe.common.exception.ExceptionCode.AUTHORIZATION_ISSUE;
import static com.withus.withmebe.common.exception.ExceptionCode.ENTITY_NOT_FOUND;

import com.withus.withmebe.chat.dto.response.ChatRoomDto;
import com.withus.withmebe.chat.entity.ChatRoom;
import com.withus.withmebe.chat.repository.ChatRoomRepository;
import com.withus.withmebe.common.exception.CustomException;
import com.withus.withmebe.gathering.entity.Gathering;
import com.withus.withmebe.gathering.repository.GatheringRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

  private final ChatRoomRepository chatRoomRepository;
  private final GatheringRepository gatheringRepository;

  public ChatRoomDto create(Long currentMemberId, Long gatheringId) {
    Gathering gathering = getGatheringById(gatheringId);
    if (!gathering.isHost(currentMemberId)) {
      throw new CustomException(AUTHORIZATION_ISSUE);
    }

    return chatRoomRepository.save(ChatRoom.builder().gathering(gathering).build()).toResponse();
  }

  private Gathering getGatheringById(Long gatheringId) {
    return gatheringRepository.findById(gatheringId)
        .orElseThrow(() -> new CustomException(ENTITY_NOT_FOUND));
  }
}
