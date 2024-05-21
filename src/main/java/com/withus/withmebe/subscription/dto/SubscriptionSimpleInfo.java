package com.withus.withmebe.subscription.dto;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record SubscriptionSimpleInfo(

    Long memberId,
    String nickName,
    String profileImg,
    LocalDateTime createdDttm
) {
}
