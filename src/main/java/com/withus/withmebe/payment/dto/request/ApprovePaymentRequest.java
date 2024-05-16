package com.withus.withmebe.payment.dto.request;

import com.withus.withmebe.payment.type.PayMethod;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record ApprovePaymentRequest(
    @NotNull
    Long id,
    @NotNull
    Long payerId,
    @NotBlank
    PayMethod payMethod,
    @NotBlank
    String tradeNo,
    @NotNull
    Long amount
) {

}
