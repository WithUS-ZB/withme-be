package com.withus.withmebe.participation.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
import com.withus.withmebe.participation.dto.GatheringParticipationSimpleInfo;
import com.withus.withmebe.participation.dto.ParticipationResponse;
import com.withus.withmebe.participation.entity.Participation;
import com.withus.withmebe.participation.repository.ParticipationRepository;
import com.withus.withmebe.participation.type.Status;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import util.StubbedObjectProvider.GatheringProvider;
import util.StubbedObjectProvider.MemberProvider;
import util.StubbedObjectProvider.ParticipationProvider;

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
  private static final Gathering STUBBED_GATHERING = GatheringProvider.getStubbedGathering(
      GATHERING_ID, HOST_ID);
  private static final Member STUBBED_PARTICIPANT = MemberProvider.getStubbedMember(PARTICIPANT_ID);
  private static final Member STUBBED_HOST = MemberProvider.getStubbedMember(HOST_ID);

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
    assertEquals(STUBBED_PARTICIPANT.getNickName(), participationResponse.nickName());
    assertEquals(STUBBED_GATHERING.getTitle(), participationResponse.title());
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
        .willReturn(Optional.of(STUBBED_GATHERING));
    given(memberRepository.findById(anyLong()))
        .willReturn(Optional.of(STUBBED_PARTICIPANT));
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
        .willReturn(Optional.of(STUBBED_GATHERING));
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
  void failToCreateParticipationByRequesterIsHost() {
    //given
    given(gatheringRepository.findById(anyLong()))
        .willReturn(Optional.of(STUBBED_GATHERING));
    given(memberRepository.findById(anyLong()))
        .willReturn(Optional.of(STUBBED_HOST));

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> participationService.createParticipation(HOST_ID, GATHERING_ID));

    //then
    assertEquals(ExceptionCode.AUTHORIZATION_ISSUE.getMessage(), exception.getMessage());
    assertEquals(HttpStatus.FORBIDDEN, exception.getHttpStatus());
  }

  @Test
  void failToCreateParticipationByParticipantsType() {
    //given
    given(gatheringRepository.findById(anyLong()))
        .willReturn(Optional.of(STUBBED_GATHERING));
    given(memberRepository.findById(anyLong()))
        .willReturn(Optional.of(MemberProvider.getStubbedMemberWithBirthDate(PARTICIPANT_ID, LocalDate.now())));

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> participationService.createParticipation(HOST_ID, GATHERING_ID));

    //then
    assertEquals(ExceptionCode.PARTICIPANTSTYPE_CONFLICT.getMessage(), exception.getMessage());
    assertEquals(HttpStatus.CONFLICT, exception.getHttpStatus());
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

  @Test
  void successToReadParticipations() {
    //given
    Participation stubbedParticipation1 = ParticipationProvider.getStubbedParticipation(1L,
        STUBBED_PARTICIPANT, STUBBED_GATHERING, Status.APPROVED);
    Member stubbedParticipant2 = MemberProvider.getStubbedMember(PARTICIPANT_ID + 1);
    Participation stubbedParticipation2 = ParticipationProvider.getStubbedParticipation(2L,
        stubbedParticipant2, STUBBED_GATHERING, Status.CREATED);

    given(gatheringRepository.findById(anyLong()))
        .willAnswer(invocationOnMock -> {
          long gatheringId = invocationOnMock.getArgument(0);
          return Optional.of(GatheringProvider.getStubbedGathering(gatheringId, HOST_ID));
        });

    given(participationRepository.findByGathering_Id(anyLong(), any(Pageable.class)))
        .willAnswer(invocationOnMock -> {
          Pageable pageable = invocationOnMock.getArgument(1);
          return new PageImpl<Participation>(List.of(stubbedParticipation1, stubbedParticipation2), pageable, 2);
        });

    //when
    Page<GatheringParticipationSimpleInfo> gatheringParticipationSimpleInfos = participationService.readParticipations(
        HOST_ID, GATHERING_ID, Pageable.ofSize(10));

    //then
    assertEquals(2, gatheringParticipationSimpleInfos.getTotalElements());
    assertEquals(1, gatheringParticipationSimpleInfos.getTotalPages());
    assertEquals(0, gatheringParticipationSimpleInfos.getNumber());

    GatheringParticipationSimpleInfo gatheringParticipationSimpleInfo1 = gatheringParticipationSimpleInfos.getContent()
        .get(0);
    assertEquals(stubbedParticipation1.getId(), gatheringParticipationSimpleInfo1.id());
    assertEquals(stubbedParticipation1.getParticipant().getNickName(), gatheringParticipationSimpleInfo1.nickName());
    assertEquals(stubbedParticipation1.getStatus(), gatheringParticipationSimpleInfo1.status());
    assertEquals(stubbedParticipation1.getUpdatedDttm(), gatheringParticipationSimpleInfo1.updatedDttm());

    GatheringParticipationSimpleInfo gatheringParticipationSimpleInfo2 = gatheringParticipationSimpleInfos.getContent()
        .get(1);
    assertEquals(stubbedParticipation2.getId(), gatheringParticipationSimpleInfo2.id());
    assertEquals(stubbedParticipation2.getParticipant().getNickName(), gatheringParticipationSimpleInfo2.nickName());
    assertEquals(stubbedParticipation2.getStatus(), gatheringParticipationSimpleInfo2.status());
    assertEquals(stubbedParticipation2.getUpdatedDttm(), gatheringParticipationSimpleInfo2.updatedDttm());
  }

  @Test
  void failToReadParticipationsByFailedToFindGathering() {
    //given
    given(gatheringRepository.findById(anyLong()))
        .willReturn(Optional.empty());

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> participationService.readParticipations(HOST_ID, GATHERING_ID, Pageable.ofSize(10)));

    //then
    assertEquals(ExceptionCode.ENTITY_NOT_FOUND.getMessage(), exception.getMessage());
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
  }

  @Test
  void failToReadParticipationsByRequesterIsNotHost() {
    //given
    given(gatheringRepository.findById(anyLong()))
        .willReturn(Optional.of(STUBBED_GATHERING));

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> participationService.readParticipations(PARTICIPANT_ID, GATHERING_ID, Pageable.ofSize(10)));

    //then
    assertEquals(ExceptionCode.AUTHORIZATION_ISSUE.getMessage(), exception.getMessage());
    assertEquals(HttpStatus.FORBIDDEN, exception.getHttpStatus());
  }
}