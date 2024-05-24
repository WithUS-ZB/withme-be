package com.withus.withmebe.notification.event;

import com.withus.withmebe.notification.response.NotificationResponse;
import com.withus.withmebe.notification.type.NotificationType;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record NotificationSendEvent(
    Long id,
    Long receiver,
    String message,
    NotificationType notificationType,
    LocalDateTime createdDttm
) {
  public NotificationResponse toResponse() {
    return NotificationResponse.builder()
        .id(this.id)
        .message(this.message)
        .notificationType(this.notificationType)
        .createdDttm(this.createdDttm.toString())
        .build();
  }

}
