package com.withus.withmebe.participation.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;

import com.withus.withmebe.common.exception.CustomException;
import com.withus.withmebe.common.exception.ExceptionCode;
import com.withus.withmebe.gathering.entity.Gathering;
import com.withus.withmebe.gathering.repository.GatheringRepository;
import com.withus.withmebe.member.entity.Member;
import com.withus.withmebe.member.repository.MemberRepository;
import com.withus.withmebe.participation.dto.ParticipationResponse;
import com.withus.withmebe.participation.entity.Participation;
import com.withus.withmebe.participation.repository.ParticipationRepository;
import com.withus.withmebe.participation.type.Status;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import util.StubbedObjectProvider.GatheringProvider;
import util.StubbedObjectProvider.MemberProvider;

@ExtendWith(MockitoExtension.class)
class ParticipationServiceTest {

  @Mock
  private GatheringRepository gatheringRepository;
  @Mock
  private ParticipationRepository participationRepository;
  @Mock
  private MemberRepository memberRepository;

  @InjectMocks
  private ParticipationService participationService;

  private static final long GATHERING_ID = 1L;
  private static final long HOST_ID = 1L;
  private static final long PARTICIPANT_ID = 2L;
  private static final Gathering stubbedGathering = GatheringProvider.getStubbedGathering(
      GATHERING_ID, HOST_ID);
  private static final Member stubbedParticipant = MemberProvider.getStubbedMember(PARTICIPANT_ID);

  @Test
  void successToCreateParticipation() {
    //given
    given(gatheringRepository.findById(anyLong()))
        .willAnswer(invocationOnMock -> {
          long gatheringId = invocationOnMock.getArgument(0);
          return Optional.of(GatheringProvider.getStubbedGathering(gatheringId, HOST_ID));
        });
    given(participationRepository.existsByParticipant_IdAndGathering_IdAndStatusIsNot(anyLong(),
        anyLong(), argThat(status -> status.equals(Status.CANCELED))))
        .willReturn(false);
    given(memberRepository.findById(anyLong()))
        .willAnswer(invocationOnMock -> {
          long participantId = invocationOnMock.getArgument(0);
          return Optional.of(MemberProvider.getStubbedMember(participantId));
        });
    given(participationRepository.save(any(Participation.class)))
        .willAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

    //when
    ParticipationResponse participationResponse = participationService.createParticipation(
        PARTICIPANT_ID, GATHERING_ID);

    //then
    assertEquals(stubbedParticipant.getNickName(), participationResponse.nickName());
    assertEquals(stubbedGathering.getTitle(), participationResponse.title());
    assertEquals(Status.CREATED, participationResponse.status());
  }

  @Test
  void failToCreateParticipationByFailedToFindGathering() {
    //given
    given(gatheringRepository.findById(anyLong()))
        .willReturn(Optional.empty());

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> participationService.createParticipation(PARTICIPANT_ID, GATHERING_ID));

    //then
    assertEquals(ExceptionCode.ENTITY_NOT_FOUND.getMessage(), exception.getMessage());
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
  }

  @Test
  void failToCreateParticipationByDuplicatedRequest() {
    //given
    given(gatheringRepository.findById(anyLong()))
        .willReturn(Optional.of(stubbedGathering));
    given(participationRepository.existsByParticipant_IdAndGathering_IdAndStatusIsNot(anyLong(),
        anyLong(), any()))
        .willReturn(true);

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> participationService.createParticipation(PARTICIPANT_ID, GATHERING_ID));

    //then
    assertEquals(ExceptionCode.PARTICIPATION_DUPLICATED.getMessage(), exception.getMessage());
    assertEquals(HttpStatus.CONFLICT, exception.getHttpStatus());
  }

  @Test
  void failToCreateParticipationByFailedToFindRequester() {
    //given
    given(gatheringRepository.findById(anyLong()))
        .willReturn(Optional.of(stubbedGathering));
    given(participationRepository.existsByParticipant_IdAndGathering_IdAndStatusIsNot(anyLong(),
        anyLong(), any()))
        .willReturn(false);
    given(memberRepository.findById(anyLong()))
        .willReturn(Optional.empty());

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> participationService.createParticipation(PARTICIPANT_ID, GATHERING_ID));

    //then
    assertEquals(ExceptionCode.ENTITY_NOT_FOUND.getMessage(), exception.getMessage());
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
  }

  @Test
  void successToReadApprovedParticipationCount() {
    //given
    given(participationRepository.countByGathering_IdAndStatus(anyLong(),
        argThat(status -> status.equals(Status.APPROVED))))
        .willReturn(2L);
    //when
    //then
    assertEquals(2L, participationService.readApprovedParticipationCount(GATHERING_ID));
  }

}