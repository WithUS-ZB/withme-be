package com.withus.withmebe.payment.entity;

import com.withus.withmebe.common.entity.BaseEntity;
import com.withus.withmebe.payment.dto.request.ApprovePaymentRequest;
import com.withus.withmebe.payment.dto.response.AddPaymentResponse;
import com.withus.withmebe.payment.dto.response.ApprovePaymentResponse;
import com.withus.withmebe.payment.type.PayMethod;
import com.withus.withmebe.payment.type.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

  @Column(nullable = false)
  private Status status = Status.CREATED;

  private PayMethod payMethod;

  @Builder
  public Payment(long payerId, String goodName, long goodPrice) {
    this.memberId = payerId;
    this.goodName = goodName;
    this.goodPrice = goodPrice;
  }

  public AddPaymentResponse toAddPaymentResponse(String siteCd, String certInfo) {
    return AddPaymentResponse.builder()
        .siteCd(siteCd)
        .id(this.id)
        .goodName(this.goodName)
        .goodPrice(this.goodPrice)
        .payerId(this.memberId)
        .certInfo(certInfo)
        .build();
  }

  public void approve(ApprovePaymentRequest request) {
    this.tradeNo = request.tradeNo();
    this.payMethod = request.payMethod();
    this.status = Status.APPROVED;
  }

  public boolean isValidApproveRequest(ApprovePaymentRequest request) {
    if (!Objects.equals(request.amount(), this.goodPrice)) {
      return false;
    }
    return Objects.equals(this.memberId, request.payerId());
  }

  public ApprovePaymentResponse toApprovePaymentResponse() {
    return ApprovePaymentResponse.builder()
        .id(this.id)
        .goodName(this.goodName)
        .goodPrice(this.goodPrice)
        .updatedDttm(this.getUpdatedDttm())
        .build();
  }
}
