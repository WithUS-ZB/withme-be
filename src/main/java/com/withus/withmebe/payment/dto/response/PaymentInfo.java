package com.withus.withmebe.payment.dto.response;

import com.withus.withmebe.payment.type.PayMethod;
import com.withus.withmebe.payment.type.Status;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record PaymentInfo(
    Long id,
    String goodName,
    Long goodPrice,
    Status status,
    PayMethod payMethod,
    String tradeNo,
    LocalDateTime updatedDttm
)
{

}
