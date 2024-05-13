package com.withus.withmebe.payment.entity;

import com.withus.withmebe.member.entity.Member;
import com.withus.withmebe.payment.dto.response.AddPaymentResponse;
import com.withus.withmebe.payment.type.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Payment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "payment_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(nullable = false, name = "member_id")
  private Member payer;

  @Column(nullable = false)
  private String goodName;

  @Column(nullable = false)
  private Long goodPrice;

  private String traceNo;

  @Column(nullable = false)
  private Status status = Status.CREATED;

  @Builder
  public Payment(Member payer, String goodName, long goodPrice) {
    this.payer = payer;
    this.goodName = goodName;
    this.goodPrice = goodPrice;
  }

  public AddPaymentResponse toAddPaymentResponse(String siteCd) {
    return AddPaymentResponse.builder()
        .siteCd(siteCd)
        .id(this.id)
        .goodName(this.goodName)
        .goodPrice(this.goodPrice)
        .payerId(this.payer.getId())
        .build();
  }
}
