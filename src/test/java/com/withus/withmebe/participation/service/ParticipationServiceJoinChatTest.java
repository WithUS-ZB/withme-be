package com.withus.withmebe.participation.service;

import static com.withus.withmebe.common.exception.ExceptionCode.AUTHORIZATION_ISSUE;
import static com.withus.withmebe.common.exception.ExceptionCode.ENTITY_NOT_FOUND;
import static com.withus.withmebe.common.exception.ExceptionCode.INVALID_TIME;
import static com.withus.withmebe.common.exception.ExceptionCode.PARTICIPATION_CONFLICT;
import static com.withus.withmebe.participation.type.Status.APPROVED;
import static com.withus.withmebe.participation.type.Status.CHAT_JOINED;
import static com.withus.withmebe.participation.type.Status.CREATED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.withus.withmebe.common.exception.CustomException;
import com.withus.withmebe.gathering.entity.Gathering;
import com.withus.withmebe.member.entity.Member;
import com.withus.withmebe.participation.entity.Participation;
import com.withus.withmebe.participation.repository.ParticipationRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import util.objectprovider.GatheringProvider;
import util.objectprovider.MemberProvider;
import util.objectprovider.ParticipationProvider;

@ExtendWith(MockitoExtension.class)
class ParticipationServiceJoinChatTest {

  @Mock
  private ParticipationRepository participationRepository;

  @InjectMocks
  private ParticipationService participationService;

  @Test
  void joinChatSuccess() {
    Long participationId = 1L;
    Long participantId = 1L;
    Long gatheringId = 1L;
    Long hostId = 2L;
    LocalDateTime now = LocalDateTime.now();
    LocalDate recruitmentStartDt = now.toLocalDate().minusDays(10);
    LocalDate recruitmentEndDt = now.toLocalDate();
    LocalDateTime gatheringDateTime = now.plusDays(5);

    Participation participation = mock(Participation.class);
    when(participation.getStatus()).thenReturn(APPROVED);
    when(participation.getGathering()).thenReturn(GatheringProvider.getStubbedGatheringByPeriodAndGatheringDateTime(
        gatheringId, hostId, recruitmentStartDt, recruitmentEndDt, gatheringDateTime));
    when(participation.isParticipant(participantId)).thenReturn(true);

    when(participationRepository.findById(participationId)).thenReturn(Optional.of(participation));

    participationService.joinChatroom(participantId, participationId);

    verify(participationRepository, times(1)).findById(participationId);
    verify(participation, times(1)).setStatus(CHAT_JOINED);
  }

  @Test
  void joinChatParticipationNotFound() {
    Long participationId = 1L;

    when(participationRepository.findById(participationId)).thenReturn(Optional.empty());

    CustomException exception = assertThrows(CustomException.class, () ->
        participationService.joinChatroom(1L, participationId)
    );

    assertEquals(ENTITY_NOT_FOUND.getStatus(), exception.getHttpStatus());
    verify(participationRepository, times(1)).findById(participationId);
  }

  @Test
  void joinChat_INVALID_TIME() {
    Long participationId = 1L;
    Long participantId = 1L;
    Long gatheringId = 1L;
    Long hostId = 2L;
    LocalDateTime now = LocalDateTime.now();
    LocalDate recruitmentStartDt = now.toLocalDate().minusDays(10);
    LocalDate recruitmentEndDt = now.toLocalDate().minusDays(2);
    LocalDateTime gatheringDateTime = now.minusDays(1);

    Member paricipant = MemberProvider.getStubbedMember(participantId);

    Gathering gathering
        = GatheringProvider.getStubbedGatheringByPeriodAndGatheringDateTime(
        gatheringId, hostId, recruitmentStartDt, recruitmentEndDt, gatheringDateTime);

    Participation stubbedParticipation = ParticipationProvider.getStubbedParticipationByStatus(
        participationId, paricipant, gathering, APPROVED);

    when(participationRepository.findById(participationId)).thenReturn(Optional.of(stubbedParticipation));

    CustomException exception = assertThrows(CustomException.class, () ->
        participationService.joinChatroom(participantId, participationId)
    );

    assertEquals(INVALID_TIME.getMessage(), exception.getMessage());
    verify(participationRepository, times(1)).findById(participationId);
  }

  @Test
  void joinChat_AUTHORIZATION_ISSUE() {
    Long participationId = 1L;
    Long participantId = 1L;
    Long gatheringId = 1L;
    Long hostId = 2L;
    Long otherMemberId= 3L;
    LocalDateTime now = LocalDateTime.now();
    LocalDate recruitmentStartDt = now.toLocalDate().minusDays(10);
    LocalDate recruitmentEndDt = now.toLocalDate();
    LocalDateTime gatheringDateTime = now.plusDays(5);

    Member paricipant = MemberProvider.getStubbedMember(participantId);

    Gathering gathering
        = GatheringProvider.getStubbedGatheringByPeriodAndGatheringDateTime(
        gatheringId, hostId, recruitmentStartDt, recruitmentEndDt, gatheringDateTime);

    Participation stubbedParticipation = ParticipationProvider.getStubbedParticipationByStatus(
        participationId, paricipant, gathering, APPROVED);

    when(participationRepository.findById(participationId)).thenReturn(Optional.of(stubbedParticipation));

    CustomException exception = assertThrows(CustomException.class, () ->
        participationService.joinChatroom(otherMemberId, participationId)
    );

    assertEquals(AUTHORIZATION_ISSUE.getMessage(), exception.getMessage());
    verify(participationRepository, times(1)).findById(participationId);
  }

  @Test
  void joinChat_PARTICIPATION_CONFLICT() {
    Long participationId = 1L;
    Long participantId = 1L;
    Long gatheringId = 1L;
    Long hostId = 2L;
    LocalDateTime now = LocalDateTime.now();
    LocalDate recruitmentStartDt = now.toLocalDate().minusDays(10);
    LocalDate recruitmentEndDt = now.toLocalDate();
    LocalDateTime gatheringDateTime = now.plusDays(5);

    Member paricipant = MemberProvider.getStubbedMember(participantId);

    Gathering gathering
        = GatheringProvider.getStubbedGatheringByPeriodAndGatheringDateTime(
        gatheringId, hostId, recruitmentStartDt, recruitmentEndDt, gatheringDateTime);

    Participation stubbedParticipation = ParticipationProvider.getStubbedParticipationByStatus(
        participationId, paricipant, gathering, CREATED);

    when(participationRepository.findById(participationId)).thenReturn(Optional.of(stubbedParticipation));

    CustomException exception = assertThrows(CustomException.class, () ->
        participationService.joinChatroom(participantId, participationId)
    );

    assertEquals(PARTICIPATION_CONFLICT.getMessage(), exception.getMessage());
    verify(participationRepository, times(1)).findById(participationId);
  }
}