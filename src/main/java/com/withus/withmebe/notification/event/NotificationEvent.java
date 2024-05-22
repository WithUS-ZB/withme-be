package com.withus.withmebe.notification.event;

import com.withus.withmebe.notification.entity.Notification;
import com.withus.withmebe.notification.type.NotificationType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;

@Builder
public record NotificationEvent(
    List<Long> receivers,
    String message,
    NotificationType notificationType
) {

  public List<Notification> toEntities() {
    return receivers.stream().map((receiver) -> Notification.builder()
        .memberId(receiver)
        .message(this.message)
        .notificationType(this.notificationType)
        .build()
    ).collect(Collectors.toList());
  }
}
