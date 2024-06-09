package com.withus.withmebe.participation.service;


import static com.withus.withmebe.common.exception.ExceptionCode.AUTHORIZATION_ISSUE;
import static com.withus.withmebe.participation.type.Status.APPROVED;
import static com.withus.withmebe.participation.type.Status.CANCELED;
import static com.withus.withmebe.participation.type.Status.CHAT_JOINED;
import static com.withus.withmebe.participation.type.Status.CHAT_LEFT;
import static com.withus.withmebe.participation.type.Status.CREATED;
import static com.withus.withmebe.participation.type.Status.REJECTED;

import com.withus.withmebe.common.exception.CustomException;
import com.withus.withmebe.common.exception.ExceptionCode;
import com.withus.withmebe.gathering.Type.ParticipantSelectionMethod;
import com.withus.withmebe.gathering.entity.Gathering;
import com.withus.withmebe.gathering.event.DeleteGatheringEvent;
import com.withus.withmebe.gathering.repository.GatheringRepository;
import com.withus.withmebe.member.entity.Member;
import com.withus.withmebe.member.repository.MemberRepository;
import com.withus.withmebe.participation.dto.GatheringParticipationSimpleInfo;
import com.withus.withmebe.participation.dto.MyParticipationSimpleInfo;
import com.withus.withmebe.participation.dto.ParticipationResponse;
import com.withus.withmebe.participation.entity.Participation;
import com.withus.withmebe.participation.repository.ParticipationRepository;
import com.withus.withmebe.participation.status.ParticipationStatusChangerSimpleFactory;
import com.withus.withmebe.participation.type.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
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
  private final ParticipationStatusChangerSimpleFactory participationStatusChangerSimpleFactory;

  private static final int MAX_PARTICIPATION_NON_PREMIUM = 5;

  public ParticipationResponse createParticipation(long requesterId, long gatheringId) {

    Gathering gathering = readGathering(gatheringId);
    Member requester = readMember(requesterId);
    validateCreateParticipationRequest(requester, gathering);

    Participation newParticipation = updateParticipationStatus(
        CREATED
        , Participation.builder().gathering(gathering).participant(requester).build()
        , requesterId);

    if(gathering.getParticipantSelectionMethod().equals(ParticipantSelectionMethod.FIRST_COME)){
      newParticipation.setStatus(APPROVED);
    }

    return participationRepository.save(newParticipation).toResponse();
  }

  public void createParticipationByHost(Long currentMemberId, Long gatheringId) {
    Gathering gathering = readGathering(gatheringId);
    if (!gathering.isHost(currentMemberId)) {
      throw new CustomException(AUTHORIZATION_ISSUE);
    }
    Member currentMember = readMember(currentMemberId);
    participationRepository.save(Participation.builder()
        .gathering(gathering)
        .participant(currentMember)
        .status(CHAT_JOINED)
        .build());
  }

  @Transactional(readOnly = true)
  public Long readApprovedParticipationCount(long gatheringId) {
    return participationRepository.countByGathering_IdAndStatus(gatheringId, APPROVED);
  }

  @Transactional(readOnly = true)
  public Page<GatheringParticipationSimpleInfo> readParticipations(
      long requesterId, long gatheringId, Pageable pageble) {

    if (!readGathering(gatheringId).isHost(requesterId)) {
      throw new CustomException(ExceptionCode.AUTHORIZATION_ISSUE);
    }

    return participationRepository.findByGathering_Id(gatheringId, pageble)
        .map(Participation::toGatheringParticipationSimpleInfo);
  }

  public ParticipationResponse cancelParticipation(long requesterId, long participationId) {
    updateParticipationStatus(CANCELED, readParticipation(participationId), requesterId);

    return readParticipation(participationId).toResponse();
  }

  public ParticipationResponse approveParticipation(long requesterId, long participationId) {
    updateParticipationStatus(APPROVED, readParticipation(participationId), requesterId);

    return readParticipation(participationId).toResponse();
  }

  public ParticipationResponse rejectParticipation(long requesterId, long participationId) {
    updateParticipationStatus(REJECTED, readParticipation(participationId), requesterId);

    return readParticipation(participationId).toResponse();
  }

  @Transactional(readOnly = true)
  public boolean isParticipated(long requesterId, long gatheringId) {
    return participationRepository.existsByParticipant_IdAndGathering_IdAndStatusIsNot(requesterId,
        gatheringId, Status.CANCELED);
  }

  @Transactional(readOnly = true)
  public Page<MyParticipationSimpleInfo> readMyParticipations(long requesterId, Pageable pageble) {
    return participationRepository.findByParticipant_IdAndStatusIsNot(
        requesterId, Status.CANCELED, pageble).map(Participation::toMyParticipationSimpleInfo);
  }


  public void joinChatroom(Long currentMemberId, Long participationId) {
    updateParticipationStatus(CHAT_JOINED, readParticipation(participationId), currentMemberId);
  }

  public void leaveChatroom(Long currentMemberId, Long participationId) {
    updateParticipationStatus(CHAT_LEFT, readParticipation(participationId), currentMemberId);
  }

  @Transactional
  public Participation updateParticipationStatus(Status status, Participation participation, Long memberId){
    return participationStatusChangerSimpleFactory.getChangeable(status, participation, memberId)
        .updateStatusTemplateMethod();
  }

  @EventListener
  protected void deleteGatheringParticipations(DeleteGatheringEvent deleteGatheringEvent) {
    participationRepository.deleteAll(
        participationRepository.findAllByGathering_Id(deleteGatheringEvent.gatheringId()));

  }

  private void validateCreateParticipationRequest(Member requester, Gathering gathering) {
    if (!gathering.getParticipantsType().isEligible(requester)) {
      throw new CustomException(ExceptionCode.PARTICIPANTSTYPE_CONFLICT);
    }

    if (isParticipated(requester.getId(), gathering.getId())) {
      throw new CustomException(ExceptionCode.PARTICIPATION_DUPLICATED);
    }

    if (!requester.isPremiumMember() &&
        participationRepository.countByParticipant_IdAndStatusIsNot(requester.getId(),
            Status.CANCELED) >= MAX_PARTICIPATION_NON_PREMIUM) {
      throw new CustomException(ExceptionCode.REACHED_AT_MAXIMUM_PARTICIPATION);
    }

    if (participationRepository.countByGatheringAndStatus(gathering, APPROVED)
        >= gathering.getMaximumParticipant()) {
      throw new CustomException(ExceptionCode.REACHED_AT_MAXIMUM_PARTICIPANT);
    }
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
