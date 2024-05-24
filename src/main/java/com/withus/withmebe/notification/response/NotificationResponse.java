package com.withus.withmebe.notification.response;

import com.withus.withmebe.notification.type.NotificationType;
import lombok.Builder;

@Builder
public record NotificationResponse(
    Long id,
    String message,
    NotificationType notificationType,
    String readDttm,
    String createdDttm
) {

}
