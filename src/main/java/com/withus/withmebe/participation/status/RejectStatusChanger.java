package com.withus.withmebe.participation.status;

import static com.withus.withmebe.participation.type.Status.CREATED;
import static com.withus.withmebe.participation.type.Status.REJECTED;

import com.withus.withmebe.participation.entity.Participation;
import com.withus.withmebe.participation.type.Status;
import java.util.List;

public class RejectStatusChanger extends ParticipationStatusChanger{
  private static final Status newStatus = REJECTED;
  private static final List<Status> availableStatus = List.of(CREATED);
  public RejectStatusChanger(
      Participation participation, Long currentMemberId
  ) {
    super(participation, currentMemberId, availableStatus, newStatus);
  }

  @Override
  boolean isAvailableTime() {
    return true;
  }

  @Override
  boolean isAvailableUser() {
    return super.isHost();
  }
}