package com.withus.withmebe.participation.status;

import com.withus.withmebe.participation.entity.Participation;
import com.withus.withmebe.participation.type.Status;
import org.springframework.stereotype.Component;

@Component
public class ParticipationStatusChangerSimpleFactory {

  public ParticipationStatusChangeable getChangeable(Status status, Participation participation, Long currentMemberId) {
    try {
      Class<? extends ParticipationStatusChangeable> clazz = status.getStatusChangerClass();

      return clazz.getConstructor(Participation.class, Long.class)
          .newInstance(participation, currentMemberId);
    } catch (Exception e) {
      throw new IllegalArgumentException("올바르지 않는 status 입니다: " + status, e);
    }
  }
}