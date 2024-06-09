package com.withus.withmebe.participation.status;

import static com.withus.withmebe.participation.type.Status.APPROVED;
import static com.withus.withmebe.participation.type.Status.CANCELED;
import static com.withus.withmebe.participation.type.Status.CHAT_LEFT;
import static com.withus.withmebe.participation.type.Status.CREATED;

import com.withus.withmebe.participation.entity.Participation;
import com.withus.withmebe.participation.type.Status;
import java.util.List;

public class CanceledStatusChanger extends ParticipationStatusChanger{

  private static final Status newStatus = CANCELED;
  private static final List<Status> availableStatus = List.of(CREATED, APPROVED, CHAT_LEFT);
  public CanceledStatusChanger(
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
    return super.isParticipant();
  }
}
