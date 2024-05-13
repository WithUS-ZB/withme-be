package com.withus.withmebe.payment.service;

import static com.withus.withmebe.common.exception.ExceptionCode.MEMBERSHIP_CONFLICT;

import com.withus.withmebe.common.exception.CustomException;
import com.withus.withmebe.common.exception.ExceptionCode;
import com.withus.withmebe.member.entity.Member;
import com.withus.withmebe.member.repository.MemberRepository;
import com.withus.withmebe.member.type.Membership;
import com.withus.withmebe.payment.dto.response.AddPaymentResponse;
import com.withus.withmebe.payment.entity.Payment;
import com.withus.withmebe.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

  private final PaymentRepository paymentRepository;
  private final MemberRepository memberRepository;
  private final static String SITE_CD = "T0000";
  private final static long PREMIUM_MEMBERSHIP_PRICE = 10000L;
  private final static String PREMIUM_MEMBERSHIP_NAME = "프리미엄 멤버쉽";

  @Transactional
  public AddPaymentResponse createPayment(long currentMemberId) {
    Member requester = readMember(currentMemberId);
    validateRequesterMembershipIsFree(requester);

    Payment newPayment = paymentRepository.save(Payment.builder()
        .payer(requester)
        .goodName(PREMIUM_MEMBERSHIP_NAME)
        .goodPrice(PREMIUM_MEMBERSHIP_PRICE)
        .build());
    return newPayment.toAddPaymentResponse(SITE_CD);
  }

  private void validateRequesterMembershipIsFree(Member requester) {
    if (isRequesterMemberShipIsNotFree(requester)) {
      throw new CustomException(MEMBERSHIP_CONFLICT);
    }
  }

  private boolean isRequesterMemberShipIsNotFree(Member requester) {
    return requester.getMembership() != Membership.FREE;
  }

  private Member readMember(long requesterId) {
    return memberRepository.findById(requesterId)
        .orElseThrow(() -> new CustomException(ExceptionCode.ENTITY_NOT_FOUND));
  }
}
