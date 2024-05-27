package com.withus.withmebe.payment.dto.response;

import lombok.Builder;

@Builder
public record AddPaymentResponse(
    String siteCd,
    String ordrId,
    String payMethod,
    String goodName,
    String goodMny,
    String currency,
    String shopUserId
) {

}
