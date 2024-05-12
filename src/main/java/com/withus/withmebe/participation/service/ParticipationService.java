package com.withus.withmebe.participation.service;


import static com.withus.withmebe.gathering.Type.Status.CANCELED;

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
        .status((gathering.getParticipantSelectionMethod().equals(ParticipantSelectionMethod.FIRST_COME) ?
            Status.APPROVED : Status.CREATED))
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

    validateReadParticipationsRequest(requesterId, readGathering(gatheringId));

    Page<Participation> participations = participationRepository.findByGathering_Id(gatheringId,
        pageble);
    return participations.map(Participation::toGatheringParticipationSimpleInfo);
  }


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
    validateUpdateParticipationRequest(currentMemberId, participation);

    participation.setStatus(status);
    Participation updatedParticipation = readParticipation(participationId);
    return updatedParticipation.toResponse();
  }

  @Transactional(readOnly = true)
  public ParticipationResponse readMyParticipation(long requesterId, long participationId) {
    Participation participation = readParticipation(participationId);

    if (!participation.isParticipant(requesterId)) {
      throw new CustomException(ExceptionCode.AUTHORIZATION_ISSUE);
    }
    return participation.toResponse();
  }

  @Transactional(readOnly = true)
  public Page<MyParticipationSimpleInfo> readMyParticipations(long requesterId, Pageable pageble) {
    Page<Participation> participations = participationRepository.findByParticipant_Id(requesterId,
        pageble);
    return participations.map(Participation::toMyParticipationSimpleInfo);
  }

  private void validateUpdateParticipationRequest(long requesterId, Participation participation) {
    Gathering gathering = participation.getGathering();
    if (!isHost(requesterId, gathering)) {
      throw new CustomException(ExceptionCode.AUTHORIZATION_ISSUE);
    }
    if (participation.checkStatus(Status.CANCELED)) {
      throw new CustomException(ExceptionCode.PARTICIPATION_CONFLICT);
    }
    if (isReachedAtMaximumParticipant(gathering)) {
      throw new CustomException(ExceptionCode.REACHED_AT_MAXIMUM_PARTICIPANT);
    }
  }

  private void validateCancelParticipationRequest(long requesterId, Participation participation) {
    if (!participation.isParticipant(requesterId)) {
      throw new CustomException(ExceptionCode.AUTHORIZATION_ISSUE);
    }
    if (participation.checkStatus(Status.REJECTED)) {
      throw new CustomException(ExceptionCode.PARTICIPATION_CONFLICT);
    }
  }

  private void validateCreateParticipationRequest(Member requester, Gathering gathering) {
    if (isMeetAtParticipantsType(requester, gathering)) {
      throw new CustomException(ExceptionCode.PARTICIPANTSTYPE_CONFLICT);
    }
    if (isHost(requester.getId(), gathering)) {
      throw new CustomException(ExceptionCode.AUTHORIZATION_ISSUE);
    }
    if (isCanceledGathering(gathering)) {
      throw new CustomException(ExceptionCode.GATHERING_CANCELED);
    }
    if (participationRepository.existsByParticipant_IdAndGathering_IdAndStatusIsNot(
        requester.getId(),
        gathering.getId(), Status.CANCELED)) {
      throw new CustomException(ExceptionCode.PARTICIPATION_DUPLICATED);
    }
    if (isParticipationPeriod(gathering)) {
      throw new CustomException(ExceptionCode.NOT_PARTICIPATION_PERIOD);
    }
    if (isReachedAtMaximumParticipant(gathering)) {
      throw new CustomException(ExceptionCode.REACHED_AT_MAXIMUM_PARTICIPANT);
    }
  }

  private void validateReadParticipationsRequest(long requesterId, Gathering gathering) {
    if (!isHost(requesterId, gathering)) {
      throw new CustomException(ExceptionCode.AUTHORIZATION_ISSUE);
    }
  }

  private boolean isHost(long requesterId, Gathering gathering) {
    return gathering.getMemberId() == requesterId;
  }

  private boolean isCanceledGathering(Gathering gathering) {
    return gathering.getStatus() == CANCELED;
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
    return LocalDate.now().isAfter(gathering.getRecruitmentStartDt()) && LocalDate.now().isBefore(gathering.getRecruitmentEndDt());
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
