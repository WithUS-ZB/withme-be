package com.withus.withmebe.participation.status;

import static com.withus.withmebe.participation.type.Status.APPROVED;
import static com.withus.withmebe.participation.type.Status.CHAT_JOINED;
import static com.withus.withmebe.participation.type.Status.CHAT_LEFT;

import com.withus.withmebe.participation.entity.Participation;
import com.withus.withmebe.participation.type.Status;
import java.time.LocalDateTime;
import java.util.List;

public class JoinChatStatusChanger extends ParticipationStatusChanger{
  private static final Status newStatus = CHAT_JOINED;
  private static final List<Status> availableStatus = List.of(APPROVED, CHAT_LEFT);
  public JoinChatStatusChanger(
      Participation participation, Long currentMemberId) {
    super(participation, currentMemberId, availableStatus, newStatus);
  }

  @Override
  boolean isAvailableTime() {
    LocalDateTime now = LocalDateTime.now();
    return participation.getGathering().getRecruitmentStartDt().isBefore(now.toLocalDate())
        && participation.getGathering().getGatheringDateTime().isAfter(now);
  }

  @Override
  boolean isAvailableUser() {
    return isParticipant() || isHost();
  }
}
