package com.withus.withmebe.participation.service;


import static com.withus.withmebe.gathering.Type.Status.PROGRESS;

import com.withus.withmebe.common.exception.CustomException;
import com.withus.withmebe.common.exception.ExceptionCode;
import com.withus.withmebe.gathering.Type.ParticipantSelectionMethod;
import com.withus.withmebe.gathering.Type.ParticipantsType;
import com.withus.withmebe.gathering.entity.Gathering;
import com.withus.withmebe.gathering.repository.GatheringRepository;
import com.withus.withmebe.member.entity.Member;
import com.withus.withmebe.member.repository.MemberRepository;
import com.withus.withmebe.participation.dto.MyParticipationSimpleInfo;
import com.withus.withmebe.participation.dto.ParticipationResponse;
import com.withus.withmebe.participation.dto.GatheringParticipationSimpleInfo;
import com.withus.withmebe.participation.entity.Participation;
import com.withus.withmebe.participation.repository.ParticipationRepository;
import com.withus.withmebe.participation.status.JoinChatStatusChanger;
import com.withus.withmebe.participation.type.Status;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ParticipationService {

  private final GatheringRepository gatheringRepository;
  private final ParticipationRepository participationRepository;
  private final MemberRepository memberRepository;

  @Transactional
  public ParticipationResponse createParticipation(long requesterId, long gatheringId) {

    Gathering gathering = readGathering(gatheringId);
    Member requester = readMember(requesterId);
    validateCreateParticipationRequest(requester, gathering);

    Participation newParticipation = participationRepository.save(Participation.builder()
        .gathering(gathering)
        .member(requester)
        .status(
            (gathering.getParticipantSelectionMethod().equals(ParticipantSelectionMethod.FIRST_COME)
                ? Status.APPROVED : Status.CREATED))
        .build());
    return newParticipation.toResponse();
  }

  @Transactional(readOnly = true)
  public Long readApprovedParticipationCount(long gatheringId) {
    return participationRepository.countByGathering_IdAndStatus(gatheringId, Status.APPROVED);
  }

  @Transactional(readOnly = true)
  public Page<GatheringParticipationSimpleInfo> readParticipations(long requesterId,
      long gatheringId, Pageable pageble) {

    validateRequesterIsHost(requesterId, readGathering(gatheringId));

    Page<Participation> participations = participationRepository.findByGathering_Id(gatheringId,
        pageble);
    return participations.map(Participation::toGatheringParticipationSimpleInfo);
  }

  @Transactional
  public ParticipationResponse cancelParticipation(long requesterId, long participationId) {

    Participation participation = readParticipation(participationId);
    validateCancelParticipationRequest(requesterId, participation);

    participation.setStatus(Status.CANCELED);
    Participation updatedParticipation = readParticipation(participationId);
    return updatedParticipation.toResponse();
  }

  @Transactional
  public ParticipationResponse updateParticipationStatus(long currentMemberId, long participationId,
      Status status) {

    Participation participation = readParticipation(participationId);
    validateUpdateParticipationRequest(currentMemberId, participation, status);

    participation.setStatus(status);
    Participation updatedParticipation = readParticipation(participationId);
    return updatedParticipation.toResponse();
  }

  @Transactional(readOnly = true)
  public ParticipationResponse readMyParticipation(long requesterId, long participationId) {

    Participation participation = readParticipation(participationId);
    validateRequesterIsParticipant(requesterId, participation);
    return participation.toResponse();
  }

  @Transactional(readOnly = true)
  public Page<MyParticipationSimpleInfo> readMyParticipations(long requesterId, Pageable pageble) {
    Page<Participation> participations = participationRepository.findByParticipant_Id(requesterId,
        pageble);
    return participations.map(Participation::toMyParticipationSimpleInfo);
  }
  public void joinChat(Long currentMemberId, Long participationId) {
    new JoinChatStatusChanger(
        readParticipation(participationId), currentMemberId)
        .updateStatusTemplateMethod();
  }

  private void validateCreateParticipationRequest(Member requester, Gathering gathering) {

    validateParticipantsType(requester, gathering);
    validateRequesterIsNotHost(requester, gathering);
    validateGatheringStatus(gathering);
    validateParticipationIsNotDuplicated(requester, gathering);
    validateParticipationPeriod(gathering);
    validateCurrentParticipantCount(gathering);
  }

  private void validateCancelParticipationRequest(long requesterId, Participation participation) {

    validateRequesterIsParticipant(requesterId, participation);
    validateParticipationStatusIsNot(participation, Status.REJECTED);
  }

  private void validateUpdateParticipationRequest(long requesterId, Participation participation,
      Status status) {

    validateParticipationStatusIsNot(participation, Status.CANCELED);

    Gathering gathering = participation.getGathering();
    validateRequesterIsHost(requesterId, gathering);

    if (status == Status.APPROVED) {
      validateCurrentParticipantCount(gathering);
    }
  }

  private void validateParticipantsType(Member requester, Gathering gathering) {
    if (!isMeetAtParticipantsType(requester, gathering)) {
      throw new CustomException(ExceptionCode.PARTICIPANTSTYPE_CONFLICT);
    }
  }

  private void validateRequesterIsNotHost(Member requester, Gathering gathering) {
    if (isHost(requester.getId(), gathering)) {
      throw new CustomException(ExceptionCode.AUTHORIZATION_ISSUE);
    }
  }

  private void validateGatheringStatus(Gathering gathering) {
    if (!isProgressingGathering(gathering)) {
      throw new CustomException(ExceptionCode.GATHERING_STATUS_CONFLICT);
    }
  }

  private void validateParticipationIsNotDuplicated(Member requester, Gathering gathering) {
    if (participationRepository.existsByParticipant_IdAndGathering_IdAndStatusIsNot(
        requester.getId(),
        gathering.getId(), Status.CANCELED)) {
      throw new CustomException(ExceptionCode.PARTICIPATION_DUPLICATED);
    }
  }

  private void validateParticipationPeriod(Gathering gathering) {
    if (!isParticipationPeriod(gathering)) {
      throw new CustomException(ExceptionCode.NOT_PARTICIPATION_PERIOD);
    }
  }

  private void validateCurrentParticipantCount(Gathering gathering) {
    if (isReachedAtMaximumParticipant(gathering)) {
      throw new CustomException(ExceptionCode.REACHED_AT_MAXIMUM_PARTICIPANT);
    }
  }

  private void validateRequesterIsHost(long requesterId, Gathering gathering) {
    if (!isHost(requesterId, gathering)) {
      throw new CustomException(ExceptionCode.AUTHORIZATION_ISSUE);
    }
  }

  private void validateRequesterIsParticipant(long requesterId, Participation participation) {
    if (!participation.isParticipant(requesterId)) {
      throw new CustomException(ExceptionCode.AUTHORIZATION_ISSUE);
    }
  }

  private void validateParticipationStatusIsNot(Participation participation, Status status) {
    if (participation.statusEquals(status)) {
      throw new CustomException(ExceptionCode.PARTICIPATION_CONFLICT);
    }
  }

  private boolean isHost(long requesterId, Gathering gathering) {
    return gathering.getMember().getId() == requesterId;
  }

  private boolean isProgressingGathering(Gathering gathering) {
    return gathering.getStatus() == PROGRESS;
  }

  private boolean isMeetAtParticipantsType(Member requester, Gathering gathering) {
    if (gathering.getParticipantsType().equals(ParticipantsType.ADULT)) {
      return requester.isAdult();
    } else if (gathering.getParticipantsType().equals(ParticipantsType.MINOR)) {
      return !requester.isAdult();
    }
    return true;
  }

  private boolean isReachedAtMaximumParticipant(Gathering gathering) {
    return participationRepository.countByGatheringAndStatus(gathering, Status.APPROVED)
        >= gathering.getMaximumParticipant();
  }

  private boolean isParticipationPeriod(Gathering gathering) {
    LocalDate now = LocalDate.now();
    return now.isAfter(gathering.getRecruitmentStartDt().minusDays(1))
        && now.isBefore(gathering.getRecruitmentEndDt().plusDays(1));
  }

  private Member readMember(long requesterId) {
    return memberRepository.findById(requesterId)
        .orElseThrow(() -> new CustomException(ExceptionCode.ENTITY_NOT_FOUND));
  }

  private Gathering readGathering(long gatheringId) {
    return gatheringRepository.findById(gatheringId)
        .orElseThrow(() -> new CustomException(ExceptionCode.ENTITY_NOT_FOUND));
  }

  private Participation readParticipation(long participationId) {
    return participationRepository.findById(participationId)
        .orElseThrow(() -> new CustomException(ExceptionCode.ENTITY_NOT_FOUND));
  }
}
