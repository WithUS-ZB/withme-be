package com.withus.withmebe.participation.status;

import static com.withus.withmebe.common.exception.ExceptionCode.AUTHORIZATION_ISSUE;
import static com.withus.withmebe.common.exception.ExceptionCode.INVALID_TIME;
import static com.withus.withmebe.common.exception.ExceptionCode.PARTICIPATION_CONFLICT;
import static com.withus.withmebe.participation.type.Status.CREATED;

import com.withus.withmebe.common.exception.CustomException;
import com.withus.withmebe.common.exception.ExceptionCode;
import com.withus.withmebe.participation.entity.Participation;
import com.withus.withmebe.participation.type.Status;
import java.util.List;
import java.util.function.BooleanSupplier;

public abstract class ParticipationStatusChanger implements ParticipationStatusChangeable {

  protected final Participation participation;
  private final Long currentMemberId;
  private final List<Status> availableStatus;
  private final Status newStatus;

  protected ParticipationStatusChanger(Participation participation, Long currentMemberId,
      List<Status> availableStatus, Status newSatus) {
    this.participation = participation;
    this.currentMemberId = currentMemberId;
    this.availableStatus = availableStatus;
    this.newStatus = newSatus;
  }

  public final Participation updateStatusTemplateMethod() {
    validateOrThrow(this::isAvailableTime, INVALID_TIME);
    validateOrThrow(this::isAvailableUser, AUTHORIZATION_ISSUE);
    validateOrThrow(this::isAvailableStatus, PARTICIPATION_CONFLICT);

    // 상태 업데이트
    updateStatus();

    return this.participation;
  }

  private void validateOrThrow(BooleanSupplier validation, ExceptionCode exceptionCode) {
    if (!validation.getAsBoolean()) {
      throw new CustomException(exceptionCode);
    }
  }

  private void updateStatus() {
    this.participation.setStatus(this.newStatus);
  }

  private boolean isAvailableStatus() {
    Status currentStatus = this.participation.getStatus();

    if (currentStatus == null) {
      return isNewStatusCreatedWhenGatheringInProgress();
    }

    return isCurrentStatusAvailable();
  }

  private boolean isNewStatusCreatedWhenGatheringInProgress() {
    return this.newStatus == CREATED && this.participation.getGathering().isProgress();
  }

  private boolean isCurrentStatusAvailable() {
    return this.availableStatus != null && this.availableStatus.contains(
        this.participation.getStatus());
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
