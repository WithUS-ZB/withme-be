package com.withus.withmebe.payment.entity;

import com.withus.withmebe.common.entity.BaseEntity;
import com.withus.withmebe.payment.dto.request.ApprovePaymentRequest;
import com.withus.withmebe.payment.dto.request.ApproveRequestToKcp;
import com.withus.withmebe.payment.dto.response.AddPaymentResponse;
import com.withus.withmebe.payment.dto.response.PaymentResponse;
import com.withus.withmebe.payment.type.Currency;
import com.withus.withmebe.payment.type.PayType;
import com.withus.withmebe.payment.type.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Payment extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "payment_id")
  private Long id;

  @Column(nullable = false)
  private Long memberId;

  @Column(nullable = false)
  private String goodName;

  @Column(nullable = false)
  private Long goodPrice;

  private String tradeNo;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Status status = Status.CREATED;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PayType payType;

  @Builder
  public Payment(long payerId, String goodName, long goodPrice, PayType payType) {
    this.memberId = payerId;
    this.goodName = goodName;
    this.goodPrice = goodPrice;
    this.payType = payType;
  }

  public AddPaymentResponse toAddPaymentResponse(String siteCd, Currency currency) {
    return AddPaymentResponse.builder()
        .siteCd(siteCd)
        .ordrId(this.id.toString())
        .payMethod(this.payType.getCode())
        .goodName(this.goodName)
        .goodMny(this.goodPrice.toString())
        .currency(currency.toString())
        .shopUserId(this.memberId.toString())
        .build();
  }

  public PaymentResponse toPaymentResponse() {
    return PaymentResponse.builder()
        .paymentId(this.id)
        .goodName(this.goodName)
        .goodPrice(this.goodPrice)
        .status(this.status)
        .payMethod(this.payType.getValue())
        .tradeNo(this.tradeNo)
        .updatedDttm(this.getUpdatedDttm())
        .build();
  }

  public ApproveRequestToKcp toApproveRequestToKcp(String siteCd, String certInfo, ApprovePaymentRequest request) {
    return ApproveRequestToKcp.builder()
        .siteCd(siteCd)
        .kcpCertInfo(certInfo)
        .encInfo(request.getEncInfo())
        .encData(request.getEncData())
        .tranCd(request.getTranCd())
        .payType(this.payType.toString())
        .ordrNo(this.id.toString())
        .ordrMony(this.goodPrice)
        .build();
  }

  public void cancel() {
    this.status = Status.CANCELED;
  }

  public boolean isGoodPrice(ApprovePaymentRequest request) {
    return Objects.equals(this.goodPrice, request.getOrdrMony());
  }

  public boolean isStatus(Status status) {
    return this.status.equals(status);
  }

  public boolean isPayer(long memberId) {
    return this.memberId == memberId;
  }

  public void approve(String tradeNo) {
    this.tradeNo = tradeNo;
    this.status = Status.APPROVED;
  }
}
