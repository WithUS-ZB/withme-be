package com.withus.withmebe.payment.dto.response;

import lombok.Builder;

@Builder
public record AddPaymentResponse(
    String siteCd,
    Long id,
    String goodName,
    Long goodPrice
) {

}
