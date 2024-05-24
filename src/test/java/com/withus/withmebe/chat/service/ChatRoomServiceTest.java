package com.withus.withmebe.chat.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.withus.withmebe.chat.dto.response.ChatRoomDto;
import com.withus.withmebe.chat.entity.ChatRoom;
import com.withus.withmebe.chat.repository.ChatRoomRepository;
import com.withus.withmebe.common.exception.CustomException;
import com.withus.withmebe.gathering.entity.Gathering;
import com.withus.withmebe.gathering.repository.GatheringRepository;
import com.withus.withmebe.participation.service.ParticipationService;
import com.withus.withmebe.participation.type.Status;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import util.objectprovider.ChatRoomProvider;
import util.objectprovider.GatheringProvider;
import util.security.WithMockCustomUser;

@ExtendWith(MockitoExtension.class)
@WithMockCustomUser
class ChatRoomServiceTest {

  @Mock
  private ChatRoomRepository chatRoomRepository;
  @Mock
  private GatheringRepository gatheringRepository;
  @Mock
  private ParticipationService participationService;

  @InjectMocks
  private ChatRoomService chatRoomService;

  @Test
  void create() {
    Long currentMemberId = 1L;
    Long gatheringId = 1L;
    Long hostId = 1L;
    Long roomId = 1L;
    Gathering gathering = GatheringProvider.getStubbedGathering(gatheringId, hostId);
    when(gatheringRepository.findById(gatheringId)).thenReturn(Optional.of(gathering));

    when(chatRoomRepository.save(any(ChatRoom.class)))
        .thenReturn(ChatRoomProvider.getStubbedChatRoom(roomId, gathering));

    ChatRoomDto chatRoomDto = chatRoomService.create(currentMemberId, gatheringId);

    assertNotNull(chatRoomDto);
    verify(participationService, times(1)).createParticipationByHost(currentMemberId, gathering);
  }

  @Test
  void create_AUTHORIZATION_ISSUE() {
    Long currentMemberId = 1L;
    Long gatheringId = 1L;
    Long hostId = 2L;
    Gathering gathering = GatheringProvider.getStubbedGathering(gatheringId, hostId);
    when(gatheringRepository.findById(gatheringId)).thenReturn(Optional.of(gathering));

    assertThrows(CustomException.class, () -> chatRoomService.create(currentMemberId, gatheringId));
  }

  @Test
  void readMyList() {
    Long currentMemberId = 1L;
    Pageable pageable = PageRequest.of(0, 10);

    ChatRoom chatRoom = mock(ChatRoom.class);
    ChatRoomDto chatRoomDto = mock(ChatRoomDto.class);
    when(chatRoom.toDto()).thenReturn(chatRoomDto);

    Page<ChatRoom> chatRoomPage = new PageImpl<>(Collections.singletonList(chatRoom));
    when(chatRoomRepository.findChatRoomsByStatusAndParticipantId(
        Status.CHAT_JOINED, currentMemberId, pageable))
        .thenReturn(chatRoomPage);

    Page<ChatRoomDto> chatRoomDtoPage = chatRoomService.readMyList(currentMemberId, pageable);

    assertNotNull(chatRoomDtoPage);
    assertEquals(1, chatRoomDtoPage.getTotalElements());
    verify(chatRoomRepository, times(1))
        .findChatRoomsByStatusAndParticipantId(Status.CHAT_JOINED, currentMemberId, pageable);
  }
}