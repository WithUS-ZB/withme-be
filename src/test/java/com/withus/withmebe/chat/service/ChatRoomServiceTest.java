package com.withus.withmebe.chat.service;

import static com.withus.withmebe.common.exception.ExceptionCode.AUTHORIZATION_ISSUE;
import static com.withus.withmebe.common.exception.ExceptionCode.ENTITY_NOT_FOUND;
import static com.withus.withmebe.participation.type.Status.CHAT_JOINED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.withus.withmebe.chat.dto.ChatMessageDto;
import com.withus.withmebe.chat.dto.response.ChatRoomDto;
import com.withus.withmebe.chat.dto.response.ParticipationInfoOfChatroom;
import com.withus.withmebe.chat.entity.ChatMessage;
import com.withus.withmebe.chat.entity.ChatRoom;
import com.withus.withmebe.chat.repository.ChatRoomRepository;
import com.withus.withmebe.common.exception.CustomException;
import com.withus.withmebe.gathering.entity.Gathering;
import com.withus.withmebe.gathering.repository.GatheringRepository;
import com.withus.withmebe.member.dto.member.MemberInfoDto;
import com.withus.withmebe.member.entity.Member;
import com.withus.withmebe.participation.entity.Participation;
import com.withus.withmebe.participation.repository.ParticipationRepository;
import com.withus.withmebe.participation.service.ParticipationService;
import com.withus.withmebe.participation.type.Status;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import util.objectprovider.ChatMessageProvider;
import util.objectprovider.ChatRoomProvider;
import util.objectprovider.GatheringProvider;
import util.objectprovider.MemberProvider;
import util.objectprovider.ParticipationProvider;

@ExtendWith(MockitoExtension.class)
class ChatRoomServiceTest {

  @Mock
  private ChatRoomRepository chatRoomRepository;
  @Mock
  private GatheringRepository gatheringRepository;
  @Mock
  private ParticipationRepository participationRepository;
  @Mock
  private ParticipationService participationService;

  @Mock
  private ChatMessageService chatMessageService;
  @InjectMocks
  private ChatRoomService chatRoomService;
  private static final Long CURRENT_MEMBER_ID = 1L;
  private static final Long GATHERING_ID = 1L;
  private static final Long HOST_ID = 2L;
  private static final Long ROOM_ID = 1L;
  private static final Long CHAT_MESSAGE_ID = 1L;
  private static final Long PARTICIPATION_ID = 1L;

  private Member currentMember;
  private Gathering gathering;
  private Participation participation;
  private ChatRoom chatRoom;
  private ChatMessage stubbedChatMessage;
  private ChatMessageDto chatMessageDto;

  @BeforeEach
  void setUp() {
    currentMember = MemberProvider.getStubbedMember(CURRENT_MEMBER_ID);
    gathering = GatheringProvider.getStubbedGathering(GATHERING_ID, HOST_ID);
    participation = ParticipationProvider.getStubbedParticipationByStatus(
        PARTICIPATION_ID, currentMember, gathering, CHAT_JOINED);
    chatRoom = ChatRoomProvider.getStubbedChatRoom(ROOM_ID, gathering);
    stubbedChatMessage = ChatMessageProvider.getStubbedChatMessage(
        CHAT_MESSAGE_ID, chatRoom, currentMember);
    chatMessageDto = stubbedChatMessage.toChatMessageDto();
  }
  @Test
  void create() {
    when(gatheringRepository.findById(GATHERING_ID)).thenReturn(Optional.of(gathering));

    when(chatRoomRepository.save(argThat(chatRoom -> chatRoom.getGathering().equals(gathering))))
        .thenReturn(chatRoom);
    ChatRoomDto chatRoomDto = chatRoomService.create(HOST_ID, GATHERING_ID);

    assertNotNull(chatRoomDto);
    verify(participationService, times(1))
        .createParticipationByHost(HOST_ID, gathering.getId());
  }

  @Test
  void create_AUTHORIZATION_ISSUE() {
    when(gatheringRepository.findById(GATHERING_ID)).thenReturn(Optional.of(gathering));

    assertThrows(CustomException.class, () -> chatRoomService.create(CURRENT_MEMBER_ID,
        GATHERING_ID));
  }

  @Test
  void join() {
    when(chatRoomRepository.findById(ROOM_ID)).thenReturn(Optional.of(chatRoom));
    when(chatMessageService.join(CURRENT_MEMBER_ID, ROOM_ID)).thenReturn(chatMessageDto);

    ChatMessageDto result = chatRoomService.join(CURRENT_MEMBER_ID, ROOM_ID, PARTICIPATION_ID);

    assertEquals(chatMessageDto, result);
    assertEquals(3, chatRoom.getMemberCount());
    verify(participationService).joinChatroom(CURRENT_MEMBER_ID, PARTICIPATION_ID);
  }

  @Test
  void leave() {
    when(chatRoomRepository.findById(ROOM_ID)).thenReturn(Optional.of(chatRoom));
    when(chatMessageService.leave(CURRENT_MEMBER_ID, ROOM_ID)).thenReturn(chatMessageDto);

    ChatMessageDto result = chatRoomService.leave(CURRENT_MEMBER_ID, ROOM_ID, PARTICIPATION_ID);

    assertEquals(chatMessageDto, result);
    assertEquals(1, chatRoom.getMemberCount());
    verify(participationService).leaveChatroom(CURRENT_MEMBER_ID, PARTICIPATION_ID);
  }

  @Test
  void readMyList() {
    Pageable pageable = PageRequest.of(0, 10);

    Page<ChatRoom> chatRoomPage = new PageImpl<>(Collections.singletonList(chatRoom));
    when(chatRoomRepository.findChatRoomsByStatusAndParticipantId(
        Status.CHAT_JOINED, CURRENT_MEMBER_ID, pageable))
        .thenReturn(chatRoomPage);

    Page<ChatRoomDto> chatRoomDtoPage = chatRoomService.readMyList(CURRENT_MEMBER_ID, pageable);

    assertNotNull(chatRoomDtoPage);
    assertEquals(1, chatRoomDtoPage.getTotalElements());
    verify(chatRoomRepository, times(1))
        .findChatRoomsByStatusAndParticipantId(Status.CHAT_JOINED, CURRENT_MEMBER_ID, pageable);
  }

  @Test
  void readParticipantsByRoom() {
    when(chatRoomRepository.existsByCurrentMemberIdAndRoomIdAndParticipationStatus(
        CURRENT_MEMBER_ID, ROOM_ID, CHAT_JOINED)).thenReturn(true);
    when(chatRoomRepository.findByParticipantOfChatroomByStatusAndRoomId(CHAT_JOINED, ROOM_ID))
        .thenReturn(List.of(currentMember));

    List<MemberInfoDto> result = chatRoomService.readParticipantsByRoom(CURRENT_MEMBER_ID, ROOM_ID);

    assertNotNull(result);
    assertEquals(1, result.size());
  }

  @Test
  void readParticipantsByRoom_AUTHORIZATION_ISSUE() {
    when(chatRoomRepository.existsByCurrentMemberIdAndRoomIdAndParticipationStatus(
        CURRENT_MEMBER_ID, ROOM_ID, CHAT_JOINED)).thenReturn(false);

    CustomException exception = assertThrows(CustomException.class, () ->
        chatRoomService.readParticipantsByRoom(CURRENT_MEMBER_ID, ROOM_ID)
    );

    assertEquals(AUTHORIZATION_ISSUE.getMessage(), exception.getMessage());
  }

  @Test
  void readParticipationJoinInfo() {
    when(chatRoomRepository.findById(ROOM_ID)).thenReturn(Optional.of(chatRoom));
    when(participationRepository.findByParticipant_IdAndGatheringAndStatus(
        CURRENT_MEMBER_ID, gathering, CHAT_JOINED))
        .thenReturn(Optional.of(participation));

    ParticipationInfoOfChatroom result = chatRoomService.readParticipationJoinInfo(
        CURRENT_MEMBER_ID, ROOM_ID);

    assertEquals(PARTICIPATION_ID, result.participationId());
    assertEquals(ROOM_ID, result.chatroomId());
    assertEquals(CURRENT_MEMBER_ID, result.memberId());
    assertEquals(CHAT_JOINED, result.participationStatus());
  }

  @Test
  void readParticipationJoinInfo_ENTITY_NOT_FOUND() {
    when(chatRoomRepository.findById(ROOM_ID)).thenReturn(Optional.of(chatRoom));
    when(participationRepository.findByParticipant_IdAndGatheringAndStatus(
        CURRENT_MEMBER_ID, gathering, CHAT_JOINED))
        .thenReturn(Optional.empty());

    CustomException exception = assertThrows(CustomException.class, () ->
        chatRoomService.readParticipationJoinInfo(CURRENT_MEMBER_ID, ROOM_ID)
    );

    assertEquals(ENTITY_NOT_FOUND.getMessage(), exception.getMessage());
  }
}