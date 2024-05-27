package com.withus.withmebe.payment.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApprovePaymentRequest{
    @NotBlank
    private String encData;
    @NotBlank
    private String encInfo;
    @NotBlank
    private String tranCd;
    @NotBlank
    private String ordrChk;

    public long getOrdrId() {
        String ordrId = this.ordrChk.split("[|]")[0];
        return Long.parseLong(ordrId);
    }

    public long getOrdrMony() {
        String ordrMony = this.ordrChk.split("[|]")[1];
        return Long.parseLong(ordrMony);
    }
}
