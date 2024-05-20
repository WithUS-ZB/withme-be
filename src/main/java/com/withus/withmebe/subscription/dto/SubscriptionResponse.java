package com.withus.withmebe.subscription.dto;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record SubscriptionResponse(

    Long id,
    Long subscriberId,
    Long providerId,
    LocalDateTime createdDttm
) {
}
