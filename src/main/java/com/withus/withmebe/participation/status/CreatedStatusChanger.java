package com.withus.withmebe.participation.status;

import static com.withus.withmebe.participation.type.Status.CREATED;

import com.withus.withmebe.participation.entity.Participation;
import com.withus.withmebe.participation.type.Status;
import java.time.LocalDate;

public class CreatedStatusChanger extends ParticipationStatusChanger{

  private static final Status newStatus = CREATED;

  public CreatedStatusChanger(
      Participation participation, Long currentMemberId
  ) {
    super(participation, currentMemberId, null, newStatus);
  }

  @Override
  boolean isAvailableTime() {
    LocalDate now = LocalDate.now();
    return (participation.getGathering().getRecruitmentStartDt().isBefore(now)
            && participation.getGathering().getRecruitmentEndDt().isAfter(now))
        || participation.getGathering().getRecruitmentStartDt().isEqual(now)
        || participation.getGathering().getRecruitmentEndDt().isEqual(now);
  }

  @Override
  boolean isAvailableUser() {
    return !super.isHost();
  }
}