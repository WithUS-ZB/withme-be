package com.withus.withmebe.participation.service;


import static com.withus.withmebe.gathering.Type.Status.CANCELED;

import com.withus.withmebe.common.exception.CustomException;
import com.withus.withmebe.common.exception.ExceptionCode;
import com.withus.withmebe.gathering.entity.Gathering;
import com.withus.withmebe.gathering.repository.GatheringRepository;
import com.withus.withmebe.member.entity.Member;
import com.withus.withmebe.member.repository.MemberRepository;
import com.withus.withmebe.participation.dto.ParticipationResponse;
import com.withus.withmebe.participation.dto.ParticipationSimpleInfo;
import com.withus.withmebe.participation.entity.Participation;
import com.withus.withmebe.participation.repository.ParticipationRepository;
import com.withus.withmebe.participation.type.Status;
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
    validateCreateParticipationRequest(requesterId, gathering);

    Member requester = readMember(requesterId);
    Participation newParticipation = participationRepository.save(Participation.builder()
        .gathering(gathering)
        .member(requester)
        .status(Status.CREATED)
        .build());
    return newParticipation.toResponse();
  }

  @Transactional(readOnly = true)
  public Long readApprovedParticipationCount(long gatheringId) {
    return participationRepository.countByGathering_IdAndStatus(gatheringId, Status.APPROVED);
  }

  @Transactional(readOnly = true)
  public Page<ParticipationSimpleInfo> readParticipations(long requesterId, long gatheringId,
      Pageable pageble) {

    Gathering gathering = readGathering(gatheringId);
    validateReadParticipationsRequest(requesterId, gathering);

    Page<Participation> participations = participationRepository.findByGathering(gathering,
        pageble);
    return participations.map(Participation::toSimpleInfo);
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
  public ParticipationResponse updateParticipationStatus(long currentMemberId, long participationId, Status status) {
    Participation participation = readParticipation(participationId);
    validateUpdateParticipationRequest(currentMemberId, participation);

    participation.setStatus(status);
    Participation updatedParticipation = readParticipation(participationId);
    return updatedParticipation.toResponse();
  }

  private void validateUpdateParticipationRequest(long requesterId, Participation participation) {
    if (!isHost(requesterId, participation.getGathering())) {
      throw new CustomException(ExceptionCode.AUTHORIZATION_ISSUE);
    }
  }

  private void validateCancelParticipationRequest(long requesterId, Participation participation) {
    if (!participation.isParticipant(requesterId)) {
      throw new CustomException(ExceptionCode.AUTHORIZATION_ISSUE);
    }
    if (participation.checkStatus(Status.CANCELED)) {
      throw new CustomException(ExceptionCode.PARTICIPATION_CONFLICT);
    }
  }

  private void validateCreateParticipationRequest(long requesterId, Gathering gathering) {
    if (isHost(requesterId, gathering)) {
      throw new CustomException(ExceptionCode.AUTHORIZATION_ISSUE);
    }
    if (isCanceledGathering(gathering)) {
      throw new CustomException(ExceptionCode.GATHERING_CANCELED);
    }
    if (participationRepository.existsByParticipant_IdAndStatusIsNot(requesterId, Status.CANCELED)) {
      throw new CustomException(ExceptionCode.PARTICIPATION_DUPLICATED);
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
