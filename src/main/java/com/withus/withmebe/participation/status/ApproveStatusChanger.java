package com.withus.withmebe.participation.status;

import static com.withus.withmebe.participation.type.Status.APPROVED;
import static com.withus.withmebe.participation.type.Status.CREATED;
import static com.withus.withmebe.participation.type.Status.REJECTED;

import com.withus.withmebe.participation.entity.Participation;
import com.withus.withmebe.participation.type.Status;
import java.time.LocalDateTime;
import java.util.List;

public class ApproveStatusChanger extends ParticipationStatusChanger{
  private static final Status newStatus = APPROVED;
  private static final List<Status> availableStatus = List.of(CREATED, REJECTED);

  public ApproveStatusChanger(
      Participation participation, Long currentMemberId
  ) {
    super(participation, currentMemberId, availableStatus, newStatus);
  }

  @Override
  boolean isAvailableTime() {
    LocalDateTime now = LocalDateTime.now();
    return participation.getGathering().getGatheringDateTime().isAfter(now);
  }

  @Override
  boolean isAvailableUser() {
    return super.isHost();
  }
}
