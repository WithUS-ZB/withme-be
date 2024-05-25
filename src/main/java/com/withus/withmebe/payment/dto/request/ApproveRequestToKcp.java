package com.withus.withmebe.payment.dto.request;

import com.google.gson.annotations.SerializedName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ApproveRequestToKcp(
    @NotBlank
    @SerializedName("site_cd")
    String siteCd,
    @NotBlank
    @SerializedName("kcp_cert_info")
    String kcpCertInfo,
    @NotBlank
    @SerializedName("enc_info")
    String encInfo,
    @NotBlank
    @SerializedName("enc_data")
    String encData,
    @NotBlank
    @SerializedName("tran_cd")
    String tranCd,
    @NotBlank
    @SerializedName("pay_type")
    String payType,
    @NotBlank
    @SerializedName("ordr_no")
    String ordrNo,
    @NotNull
    @SerializedName("ordr_mony")
    Long ordrMony
) {

}
