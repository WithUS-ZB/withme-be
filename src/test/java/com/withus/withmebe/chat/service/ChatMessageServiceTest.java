package com.withus.withmebe.chat.service;

import static com.withus.withmebe.common.exception.ExceptionCode.AUTHORIZATION_ISSUE;
import static com.withus.withmebe.participation.type.Status.CHAT_JOINED;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.withus.withmebe.chat.dto.ChatMessageDto;
import com.withus.withmebe.chat.dto.request.ChatMessageRequestDto;
import com.withus.withmebe.chat.entity.ChatMessage;
import com.withus.withmebe.chat.entity.ChatRoom;
import com.withus.withmebe.chat.repository.ChatMessageRepository;
import com.withus.withmebe.chat.repository.ChatRoomRepository;
import com.withus.withmebe.common.exception.CustomException;
import com.withus.withmebe.gathering.entity.Gathering;
import com.withus.withmebe.member.entity.Member;
import com.withus.withmebe.member.repository.MemberRepository;
import com.withus.withmebe.participation.entity.Participation;
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

import java.util.Collections;
import java.util.Optional;
import util.objectprovider.ChatMessageProvider;
import util.objectprovider.ChatRoomProvider;
import util.objectprovider.GatheringProvider;
import util.objectprovider.MemberProvider;
import util.objectprovider.ParticipationProvider;

@ExtendWith(MockitoExtension.class)
class ChatMessageServiceTest {

  @Mock
  private ChatMessageRepository chatMessageRepository;

  @Mock
  private ChatRoomRepository chatRoomRepository;

  @Mock
  private MemberRepository memberRepository;

  @InjectMocks
  private ChatMessageService chatMessageService;

  private final static Long CURRENT_MEMBER_ID = 1L;
  private final static Long GATHERING_ID = 1L;
  private final static Long HOST_ID = 2L;
  private final static Long ROOM_ID = 1L;
  private final static Long CHAT_MESSAGE_ID = 1L;
  private final static Long PARTICIPATION_ID = 1L;

  private Member currentMember;
  private Gathering gathering;
  private Participation participation;
  private ChatRoom chatRoom;
  private ChatMessage chatMessage;
  private ChatMessageDto chatMessageDto;

  @BeforeEach
  void setUp() {
    currentMember = MemberProvider.getStubbedMember(CURRENT_MEMBER_ID);
    gathering = GatheringProvider.getStubbedGathering(GATHERING_ID, HOST_ID);
    participation = ParticipationProvider.getStubbedParticipationByStatus(
        PARTICIPATION_ID, currentMember, gathering, CHAT_JOINED);
    chatRoom = ChatRoomProvider.getStubbedChatRoom(ROOM_ID, gathering);
    chatMessage = ChatMessageProvider.getStubbedChatMessage(
        CHAT_MESSAGE_ID, chatRoom, currentMember);
    chatMessageDto = chatMessage.toChatMessageDto();
  }

  @Test
  void join() {
    when(chatRoomRepository.findById(ROOM_ID)).thenReturn(Optional.of(chatRoom));
    when(memberRepository.findById(CURRENT_MEMBER_ID)).thenReturn(Optional.of(currentMember));
    when(chatMessageRepository.save(argThat(chatMessage
        -> chatMessage.getChatRoom().equals(chatRoom)
        && chatMessage.getChatMember().equals(currentMember)))).thenReturn(chatMessage);

    ChatMessageDto result = chatMessageService.join(CURRENT_MEMBER_ID, ROOM_ID);

    assertNotNull(result);
    assertEquals(chatMessage.getContent(), result.content());
  }

  @Test
  void leave() {
    when(chatRoomRepository.findById(ROOM_ID)).thenReturn(Optional.of(chatRoom));
    when(memberRepository.findById(CURRENT_MEMBER_ID)).thenReturn(Optional.of(currentMember));
    when(chatMessageRepository.save(argThat(chatMessage
        -> chatMessage.getChatRoom().equals(chatRoom)
        && chatMessage.getChatMember().equals(currentMember)))).thenReturn(chatMessage);

    ChatMessageDto result = chatMessageService.leave(CURRENT_MEMBER_ID, ROOM_ID);

    assertNotNull(result);
    assertEquals(chatMessage.getContent(), result.content());
  }

  @Test
  void chat() {
    when(chatRoomRepository.findById(ROOM_ID)).thenReturn(Optional.of(chatRoom));
    when(memberRepository.findById(CURRENT_MEMBER_ID)).thenReturn(Optional.of(currentMember));
    when(chatMessageRepository.save(argThat(chatMessage
    -> chatMessage.getChatRoom().equals(chatRoom)
    && chatMessage.getChatMember().equals(currentMember)))).thenReturn(chatMessage);

    ChatMessageDto result = chatMessageService.chat(
        CURRENT_MEMBER_ID, new ChatMessageRequestDto(ROOM_ID, chatMessage.getContent()));

    assertNotNull(result);
    assertEquals(chatMessage.getContent(), result.content());
    verify(chatRoomRepository).findById(ROOM_ID);
    verify(memberRepository).findById(CURRENT_MEMBER_ID);
  }

  @Test
  void readListByRoomId() {
    Pageable pageable = PageRequest.of(0, 10);
    Page<ChatMessage> chatMessagePage = new PageImpl<>(Collections.singletonList(chatMessage), pageable, 1);
    when(chatRoomRepository.existsByCurrentMemberIdAndRoomIdAndParticipationStatus(
        CURRENT_MEMBER_ID, ROOM_ID, CHAT_JOINED)).thenReturn(true);
    when(chatRoomRepository.findById(ROOM_ID)).thenReturn(Optional.of(chatRoom));
    when(chatMessageRepository.findByChatRoom(chatRoom, pageable))
        .thenReturn(chatMessagePage);

    Page<ChatMessageDto> result = chatMessageService.readListByRoomId(CURRENT_MEMBER_ID, ROOM_ID, pageable);

    assertNotNull(result);
    assertEquals(1, result.getTotalElements());
  }

  @Test
  void readListByRoomId_AUTHORIZATION_ISSUE() {
    when(chatRoomRepository.existsByCurrentMemberIdAndRoomIdAndParticipationStatus(
        CURRENT_MEMBER_ID, ROOM_ID, CHAT_JOINED))
        .thenReturn(false);

    CustomException exception = assertThrows(CustomException.class, () ->
        chatMessageService.readListByRoomId(CURRENT_MEMBER_ID, ROOM_ID, PageRequest.of(0, 10))
    );

    assertEquals(AUTHORIZATION_ISSUE.getMessage(), exception.getMessage());
  }
}
