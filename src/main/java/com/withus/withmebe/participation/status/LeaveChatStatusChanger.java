package com.withus.withmebe.participation.status;

import static com.withus.withmebe.participation.type.Status.CHAT_JOINED;
import static com.withus.withmebe.participation.type.Status.CHAT_LEFT;

import com.withus.withmebe.participation.entity.Participation;
import com.withus.withmebe.participation.type.Status;
import java.time.LocalDate;
import java.util.List;

public class LeaveChatStatusChanger extends ParticipationStatusChanger{
  private static final List<Status> availableStatus = List.of(CHAT_JOINED);
  private static final Status newStatus = CHAT_LEFT;
  public LeaveChatStatusChanger(
      Participation participation, Long currentMemberId
      ) {
    super(participation, currentMemberId, availableStatus, newStatus);
  }

  @Override
  boolean isAvailableTime() {
    return participation.getGathering().getRecruitmentStartDt().isBefore(LocalDate.now());
  }

  @Override
  boolean isAvailableUser() {
    return isParticipant() || isHost();
  }
}
