package com.withus.withmebe.notification.response;

import com.withus.withmebe.notification.type.NotificationType;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.ToString;

@Builder
public record NotificationResponse(
    Long id,
    String message,
    NotificationType notificationType,
    LocalDateTime readDttm,
    LocalDateTime createdDttm
) {

}
