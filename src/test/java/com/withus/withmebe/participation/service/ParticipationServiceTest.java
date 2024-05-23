package com.withus.withmebe.participation.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static util.objectprovider.GatheringProvider.getStubbedGathering;
import static util.objectprovider.GatheringProvider.getStubbedGatheringPeriodAlwaysFalse;
import static util.objectprovider.MemberProvider.getStubbedMember;
import static util.objectprovider.MemberProvider.getStubbedMinorMember;
import static util.objectprovider.ParticipationProvider.getStubbedParticipation;
import static util.objectprovider.ParticipationProvider.getStubbedParticipationByStatus;

import com.withus.withmebe.common.exception.CustomException;
import com.withus.withmebe.common.exception.ExceptionCode;
import com.withus.withmebe.gathering.entity.Gathering;
import com.withus.withmebe.gathering.repository.GatheringRepository;
import com.withus.withmebe.member.entity.Member;
import com.withus.withmebe.member.repository.MemberRepository;
import com.withus.withmebe.participation.dto.GatheringParticipationSimpleInfo;
import com.withus.withmebe.participation.dto.MyParticipationSimpleInfo;
import com.withus.withmebe.participation.dto.ParticipationResponse;
import com.withus.withmebe.participation.entity.Participation;
import com.withus.withmebe.participation.repository.ParticipationRepository;
import com.withus.withmebe.participation.type.Status;
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
  private static final long PARTICIPATION_ID = 3L;
  private static final Gathering STUBBED_GATHERING = getStubbedGathering(
      GATHERING_ID, HOST_ID);
  private static final Member STUBBED_PARTICIPANT = getStubbedMember(PARTICIPANT_ID);
  private static final Member STUBBED_MINOR_PARTICIPANT = getStubbedMinorMember(PARTICIPANT_ID + 1);
  private static final Pageable PAGEABLE = Pageable.ofSize(10);
  private static final Participation STUBBED_PARTICIPATION = getStubbedParticipation(
      PARTICIPATION_ID, STUBBED_PARTICIPANT,
      STUBBED_GATHERING);

  @Test
  void successToCreateParticipation() {
    //given
    given(gatheringRepository.findById(anyLong()))
        .willReturn(Optional.of(STUBBED_GATHERING));
    given(participationRepository.existsByParticipant_IdAndGathering_IdAndStatusIsNot(anyLong(),
        anyLong(), argThat(status -> status.equals(Status.CANCELED))))
        .willReturn(false);
    given(memberRepository.findById(anyLong()))
        .willReturn(Optional.of(STUBBED_PARTICIPANT));
    given(participationRepository.save(any(Participation.class)))
        .willReturn(STUBBED_PARTICIPATION);

    //when
    ParticipationResponse participationResponse = participationService.createParticipation(
        PARTICIPANT_ID, GATHERING_ID);

    //then
    assertEquals(STUBBED_PARTICIPATION.getId(), participationResponse.id());
    assertEquals(STUBBED_PARTICIPATION.getParticipant().getNickName(),
        participationResponse.nickName());
    assertEquals(STUBBED_PARTICIPATION.getGathering().getTitle(), participationResponse.title());
    assertEquals(STUBBED_PARTICIPATION.getStatus(), participationResponse.status());
    assertEquals(STUBBED_PARTICIPATION.getCreatedDttm(), participationResponse.createdDttm());
    assertEquals(STUBBED_PARTICIPATION.getUpdatedDttm(), participationResponse.updatedDttm());
  }

  @Test
  void failToCreateParticipationByFailedToReadGathering() {
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
  void failToCreateParticipationByFailedToReadRequester() {
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
  void failToCreateParticipationByParticipantsType() {
    //given
    given(gatheringRepository.findById(anyLong()))
        .willReturn(Optional.of(STUBBED_GATHERING));
    given(memberRepository.findById(anyLong()))
        .willReturn(Optional.of(STUBBED_MINOR_PARTICIPANT));

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> participationService.createParticipation(HOST_ID, GATHERING_ID));

    //then
    assertEquals(ExceptionCode.PARTICIPANTSTYPE_CONFLICT.getMessage(), exception.getMessage());
    assertEquals(HttpStatus.CONFLICT, exception.getHttpStatus());
  }

  @Test
  void failToCreateParticipationByRequesterIsHost() {
    //given
    given(gatheringRepository.findById(anyLong()))
        .willReturn(Optional.of(STUBBED_GATHERING));
    given(memberRepository.findById(anyLong()))
        .willReturn(Optional.of(getStubbedMember(HOST_ID)));

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> participationService.createParticipation(HOST_ID, GATHERING_ID));

    //then
    assertEquals(ExceptionCode.AUTHORIZATION_ISSUE.getMessage(), exception.getMessage());
    assertEquals(HttpStatus.FORBIDDEN, exception.getHttpStatus());
  }

  @Test
  void failToCreateParticipationByGatheringStatus() {
    //given
    given(gatheringRepository.findById(anyLong()))
        .willReturn(Optional.of(getStubbedGathering(GATHERING_ID + 1, HOST_ID)));
    given(memberRepository.findById(anyLong()))
        .willReturn(Optional.of(STUBBED_MINOR_PARTICIPANT));

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> participationService.createParticipation(HOST_ID, GATHERING_ID));

    //then
    assertEquals(ExceptionCode.GATHERING_STATUS_CONFLICT.getMessage(), exception.getMessage());
    assertEquals(HttpStatus.CONFLICT, exception.getHttpStatus());
  }

  @Test
  void failToCreateParticipationByDuplicatedRequest() {
    //given
    given(gatheringRepository.findById(anyLong()))
        .willReturn(Optional.of(STUBBED_GATHERING));
    given(memberRepository.findById(anyLong()))
        .willReturn(Optional.of(STUBBED_PARTICIPANT));
    given(participationRepository.existsByParticipant_IdAndGathering_IdAndStatusIsNot(anyLong(),
        anyLong(), any(Status.class)))
        .willReturn(true);

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> participationService.createParticipation(PARTICIPANT_ID, GATHERING_ID));

    //then
    assertEquals(ExceptionCode.PARTICIPATION_DUPLICATED.getMessage(), exception.getMessage());
    assertEquals(HttpStatus.CONFLICT, exception.getHttpStatus());
  }

  @Test
  void failToCreateParticipationByPeriod() {
    //given
    given(gatheringRepository.findById(anyLong()))
        .willReturn(Optional.of(getStubbedGatheringPeriodAlwaysFalse(GATHERING_ID, HOST_ID)));
    given(memberRepository.findById(anyLong()))
        .willReturn(Optional.of(STUBBED_PARTICIPANT));
    given(participationRepository.existsByParticipant_IdAndGathering_IdAndStatusIsNot(anyLong(),
        anyLong(), argThat(status -> status.equals(Status.CANCELED))))
        .willReturn(false);

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> participationService.createParticipation(PARTICIPANT_ID, GATHERING_ID));

    //then
    assertEquals(ExceptionCode.NOT_PARTICIPATION_PERIOD.getMessage(), exception.getMessage());
    assertEquals(HttpStatus.CONFLICT, exception.getHttpStatus());
  }

  @Test
  void failToCreateParticipationByParticipantCount() {
    //given
    given(gatheringRepository.findById(anyLong()))
        .willReturn(Optional.of(STUBBED_GATHERING));
    given(memberRepository.findById(anyLong()))
        .willReturn(Optional.of(STUBBED_PARTICIPANT));
    given(participationRepository.existsByParticipant_IdAndGathering_IdAndStatusIsNot(anyLong(),
        anyLong(), argThat(status -> status.equals(Status.CANCELED))))
        .willReturn(false);
    given(participationRepository.countByGatheringAndStatus(any(Gathering.class),
        argThat(status -> status.equals(Status.APPROVED))))
        .willReturn(STUBBED_GATHERING.getMaximumParticipant());

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> participationService.createParticipation(PARTICIPANT_ID, GATHERING_ID));

    //then
    assertEquals(ExceptionCode.REACHED_AT_MAXIMUM_PARTICIPANT.getMessage(), exception.getMessage());
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
    Participation stubbedParticipation1 = getStubbedParticipationByStatus(PARTICIPATION_ID,
        STUBBED_PARTICIPANT, STUBBED_GATHERING, Status.APPROVED);
    Member stubbedParticipant2 = getStubbedMember(PARTICIPANT_ID + 1);
    Participation stubbedParticipation2 = getStubbedParticipationByStatus(PARTICIPATION_ID + 1,
        stubbedParticipant2, STUBBED_GATHERING, Status.CREATED);

    given(gatheringRepository.findById(anyLong()))
        .willReturn(Optional.of(STUBBED_GATHERING));
    given(participationRepository.findByGathering_Id(anyLong(), any(Pageable.class)))
        .willAnswer(
            invocationOnMock -> {
              Pageable pageable = invocationOnMock.getArgument(1);
              return new PageImpl<Participation>(
                  List.of(stubbedParticipation1, stubbedParticipation2),
                  pageable, 2);
            });

    //when
    Page<GatheringParticipationSimpleInfo> gatheringParticipationSimpleInfos = participationService.readParticipations(
        HOST_ID, GATHERING_ID, PAGEABLE);

    //then
    assertEquals(2, gatheringParticipationSimpleInfos.getTotalElements());
    assertEquals(1, gatheringParticipationSimpleInfos.getTotalPages());
    assertEquals(0, gatheringParticipationSimpleInfos.getNumber());

    GatheringParticipationSimpleInfo gatheringParticipationSimpleInfo1 = gatheringParticipationSimpleInfos.getContent()
        .get(0);
    assertEquals(stubbedParticipation1.getId(), gatheringParticipationSimpleInfo1.id());
    assertEquals(stubbedParticipation1.getParticipant().getNickName(),
        gatheringParticipationSimpleInfo1.nickName());
    assertEquals(stubbedParticipation1.getStatus(), gatheringParticipationSimpleInfo1.status());
    assertEquals(stubbedParticipation1.getUpdatedDttm(),
        gatheringParticipationSimpleInfo1.updatedDttm());

    GatheringParticipationSimpleInfo gatheringParticipationSimpleInfo2 = gatheringParticipationSimpleInfos.getContent()
        .get(1);
    assertEquals(stubbedParticipation2.getId(), gatheringParticipationSimpleInfo2.id());
    assertEquals(stubbedParticipation2.getParticipant().getNickName(),
        gatheringParticipationSimpleInfo2.nickName());
    assertEquals(stubbedParticipation2.getStatus(), gatheringParticipationSimpleInfo2.status());
    assertEquals(stubbedParticipation2.getUpdatedDttm(),
        gatheringParticipationSimpleInfo2.updatedDttm());
  }

  @Test
  void failToReadParticipationsByFailedToReadGathering() {
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
        () -> participationService.readParticipations(PARTICIPANT_ID, GATHERING_ID,
            Pageable.ofSize(10)));

    //then
    assertEquals(ExceptionCode.AUTHORIZATION_ISSUE.getMessage(), exception.getMessage());
    assertEquals(HttpStatus.FORBIDDEN, exception.getHttpStatus());
  }

  @Test
  void successToCancelParticipation() {
    //given
    given(participationRepository.findById(anyLong()))
        .willReturn(Optional.of(STUBBED_PARTICIPATION));

    //when
    ParticipationResponse participationResponse = participationService.cancelParticipation(
        PARTICIPANT_ID, PARTICIPATION_ID);

    //then
    assertEquals(STUBBED_PARTICIPATION.getId(), participationResponse.id());
    assertEquals(STUBBED_PARTICIPATION.getParticipant().getNickName(),
        participationResponse.nickName());
    assertEquals(STUBBED_PARTICIPATION.getGathering().getTitle(), participationResponse.title());
    assertEquals(Status.CANCELED, participationResponse.status());
    assertEquals(STUBBED_PARTICIPATION.getCreatedDttm(), participationResponse.createdDttm());
    assertEquals(STUBBED_PARTICIPATION.getUpdatedDttm(), participationResponse.updatedDttm());
  }

  @Test
  void failToCancelParticipationByFailedToReadParticipation() {
    //given
    given(participationRepository.findById(anyLong()))
        .willReturn(Optional.empty());

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> participationService.cancelParticipation(PARTICIPANT_ID, PARTICIPATION_ID));

    //then
    assertEquals(ExceptionCode.ENTITY_NOT_FOUND.getMessage(), exception.getMessage());
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
  }

  @Test
  void failToCancelParticipationRequesterIsNotParticipant() {
    //given
    given(participationRepository.findById(anyLong()))
        .willReturn(Optional.of(STUBBED_PARTICIPATION));

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> participationService.cancelParticipation(PARTICIPANT_ID + 1, PARTICIPATION_ID));

    //then
    assertEquals(ExceptionCode.AUTHORIZATION_ISSUE.getMessage(), exception.getMessage());
    assertEquals(HttpStatus.FORBIDDEN, exception.getHttpStatus());

    STUBBED_PARTICIPATION.setStatus(Status.CREATED);
  }

  @Test
  void successToUpdateParticipationToApprove() {
    //given

    given(participationRepository.findById(anyLong()))
        .willReturn(Optional.of(STUBBED_PARTICIPATION));

    //when
    ParticipationResponse participationResponse = participationService.updateParticipationStatus(
        HOST_ID, PARTICIPATION_ID, Status.APPROVED);

    //then
    assertEquals(STUBBED_PARTICIPATION.getId(), participationResponse.id());
    assertEquals(STUBBED_PARTICIPATION.getParticipant().getNickName(),
        participationResponse.nickName());
    assertEquals(STUBBED_PARTICIPATION.getGathering().getTitle(), participationResponse.title());
    assertEquals(Status.APPROVED, participationResponse.status());
    assertEquals(STUBBED_PARTICIPATION.getCreatedDttm(), participationResponse.createdDttm());
    assertEquals(STUBBED_PARTICIPATION.getUpdatedDttm(), participationResponse.updatedDttm());
  }

  @Test
  void failToUpdateParticipationByFailedReadParticipation() {
    //given
    given(participationRepository.findById(anyLong()))
        .willReturn(Optional.empty());

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> participationService.updateParticipationStatus(HOST_ID, PARTICIPATION_ID,
            Status.APPROVED));

    //then
    assertEquals(ExceptionCode.ENTITY_NOT_FOUND.getMessage(), exception.getMessage());
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
  }

  @Test
  void failToUpdateParticipationRequesterIsNotHost() {
    //given
    given(participationRepository.findById(anyLong()))
        .willReturn(Optional.of(STUBBED_PARTICIPATION));

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> participationService.updateParticipationStatus(HOST_ID + 1, PARTICIPATION_ID,
            Status.APPROVED));

    //then
    assertEquals(ExceptionCode.AUTHORIZATION_ISSUE.getMessage(), exception.getMessage());
    assertEquals(HttpStatus.FORBIDDEN, exception.getHttpStatus());
  }

  @Test
  void failToUpdateParticipationByStatusIsCanceled() {
    //given
    given(participationRepository.findById(anyLong()))
        .willReturn(Optional.of(
            getStubbedParticipationByStatus(PARTICIPATION_ID, STUBBED_PARTICIPANT,
                STUBBED_GATHERING, Status.CANCELED)));

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> participationService.updateParticipationStatus(HOST_ID, PARTICIPATION_ID,
            Status.APPROVED));

    //then
    assertEquals(ExceptionCode.PARTICIPATION_CONFLICT.getMessage(), exception.getMessage());
    assertEquals(HttpStatus.CONFLICT, exception.getHttpStatus());
  }

  @Test
  void failToUpdateParticipationByParticipantCount() {
    //given
    given(participationRepository.findById(anyLong()))
        .willReturn(Optional.of(STUBBED_PARTICIPATION));
    given(participationRepository.countByGatheringAndStatus(any(Gathering.class),
        argThat(status -> status.equals(Status.APPROVED))))
        .willReturn(STUBBED_GATHERING.getMaximumParticipant());

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> participationService.updateParticipationStatus(HOST_ID, PARTICIPATION_ID,
            Status.APPROVED));

    //then
    assertEquals(ExceptionCode.REACHED_AT_MAXIMUM_PARTICIPANT.getMessage(), exception.getMessage());
    assertEquals(HttpStatus.CONFLICT, exception.getHttpStatus());
  }

  @Test
  void successToIsParticipatedWhenTrue() {
    //given
    given(participationRepository.existsByParticipant_IdAndGathering_IdAndStatusIsNot(anyLong(),
        anyLong(), argThat(status -> status.equals(Status.CANCELED))))
        .willReturn(true);

    //when
    //then
    assertTrue(participationService.isParticipated(PARTICIPANT_ID, GATHERING_ID));
  }

  @Test
  void successToIsParticipatedWhenFalse() {
    //given
    given(participationRepository.existsByParticipant_IdAndGathering_IdAndStatusIsNot(anyLong(),
        anyLong(), argThat(status -> status.equals(Status.CANCELED))))
        .willReturn(false);

    //when
    //then
    assertFalse(participationService.isParticipated(PARTICIPANT_ID, GATHERING_ID));
  }

  @Test
  void successToReadMyParticipations() {
    //given
    Participation stubbedParticipation2 = getStubbedParticipation(PARTICIPATION_ID + 1,
        STUBBED_PARTICIPANT, getStubbedGathering(GATHERING_ID + 1, HOST_ID));

    given(participationRepository.findByParticipant_IdAndStatusIsNot(anyLong(),
        argThat(status -> status.equals(Status.CANCELED)), any(Pageable.class)))
        .willAnswer(
            invocationOnMock -> {
              Pageable pageable = invocationOnMock.getArgument(2);
              return new PageImpl<Participation>(
                  List.of(STUBBED_PARTICIPATION, stubbedParticipation2),
                  pageable, 2);
            });

    //when
    Page<MyParticipationSimpleInfo> myParticipationSimpleInfos =
        participationService.readMyParticipations(PARTICIPANT_ID, PAGEABLE);

    //then
    assertEquals(2, myParticipationSimpleInfos.getTotalElements());
    assertEquals(1, myParticipationSimpleInfos.getTotalPages());
    assertEquals(0, myParticipationSimpleInfos.getNumber());

    MyParticipationSimpleInfo myParticipationSimpleInfo1 =
        myParticipationSimpleInfos.getContent().get(0);
    assertEquals(STUBBED_PARTICIPATION.getId(), myParticipationSimpleInfo1.id());
    assertEquals(STUBBED_PARTICIPATION.getStatus(), myParticipationSimpleInfo1.status());
    assertEquals(STUBBED_PARTICIPATION.getGathering().getTitle(),
        myParticipationSimpleInfo1.title());
    assertEquals(STUBBED_PARTICIPATION.getUpdatedDttm(), myParticipationSimpleInfo1.updatedDttm());

    MyParticipationSimpleInfo myParticipationSimpleInfo2 =
        myParticipationSimpleInfos.getContent().get(1);
    assertEquals(stubbedParticipation2.getId(), myParticipationSimpleInfo2.id());
    assertEquals(stubbedParticipation2.getStatus(), myParticipationSimpleInfo2.status());
    assertEquals(stubbedParticipation2.getGathering().getTitle(),
        myParticipationSimpleInfo2.title());
    assertEquals(stubbedParticipation2.getUpdatedDttm(), myParticipationSimpleInfo2.updatedDttm());
  }
}