package com.withus.withmebe.payment.dto.response;

import com.withus.withmebe.payment.type.Status;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record PaymentResponse(
    Long paymentId,
    String goodName,
    Long goodPrice,
    Status status,
    String payMethod,
    String tradeNo,
    LocalDateTime updatedDttm
)
{

}
