package com.withus.withmebe.payment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ApprovePaymentRequest(
    @NotBlank
    String encData,
    @NotBlank
    String encInfo,
    @NotBlank
    String tranCd,
    @NotNull
    Long ordrMony,
    @NotBlank
    Long ordrNo

) {

}
