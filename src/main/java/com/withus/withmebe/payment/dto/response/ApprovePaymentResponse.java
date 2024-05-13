package com.withus.withmebe.payment.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ApprovePaymentResponse(
    Long id,
    String goodName,
    Long goodPrice,
    LocalDateTime updatedDttm
) {

}
