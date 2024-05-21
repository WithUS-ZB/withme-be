package com.withus.withmebe.participation.status;

import static com.withus.withmebe.common.exception.ExceptionCode.AUTHORIZATION_ISSUE;
import static com.withus.withmebe.common.exception.ExceptionCode.INVALID_TIME;
import static com.withus.withmebe.common.exception.ExceptionCode.PARTICIPATION_CONFLICT;

import com.withus.withmebe.common.exception.CustomException;
import com.withus.withmebe.participation.entity.Participation;
import com.withus.withmebe.participation.type.Status;
import java.util.List;

public abstract class ParticipationStatusChanger {
  protected final Participation participation;
  private final Long currentMemberId;

  private final List<Status> availableStatus;

  private final Status newStatus;

  protected ParticipationStatusChanger(Participation participation, Long currentMemberId, List<Status> availableStatus, Status newSatus) {
    this.participation = participation;
    this.currentMemberId = currentMemberId;
    this.availableStatus = availableStatus;
    this.newStatus = newSatus;
  }

  public final Participation updateStatusTemplateMethod() {
    // 가능한 시간인가?
    validateAvailableTimeOrThrow();

    // 가능한 유저인가?
    validateAvailableUserOrThrow();

    // 가능한 상태인가?
    validateAvailableStatusOrThrow();

    // 상태 업데이트
    updateStatus();

    return this.participation;
  }

  private void validateAvailableTimeOrThrow() {
    if (!isAvailableTime()) {
      throw new CustomException(INVALID_TIME);
    }
  }

  private void validateAvailableUserOrThrow() {
    if (!isAvailableUser()) {
      throw new CustomException(AUTHORIZATION_ISSUE);
    }
  }

  private void validateAvailableStatusOrThrow() {
    if (!isAvailableStatus()) {
      throw new CustomException(PARTICIPATION_CONFLICT);
    }
  }

  private void updateStatus() {
    this.participation.setStatus(this.newStatus);
  }

  private boolean isAvailableStatus() {
    Status currentStatus = this.participation.getStatus();
    return this.availableStatus.stream()
        .anyMatch(status -> status == currentStatus);
  }

  abstract boolean isAvailableTime();

  abstract boolean isAvailableUser();

  protected boolean isHost() {
    return this.participation.getGathering().isHost(this.currentMemberId);
  }

  protected boolean isParticipant() {
    return this.participation.isParticipant(this.currentMemberId);
  }
}
