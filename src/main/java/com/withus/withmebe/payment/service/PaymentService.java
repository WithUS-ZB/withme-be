package com.withus.withmebe.payment.service;

import static com.withus.withmebe.common.exception.ExceptionCode.AUTHORIZATION_ISSUE;
import static com.withus.withmebe.common.exception.ExceptionCode.FAIL_TO_REQUEST_APPROVE_PAYMENT;
import static com.withus.withmebe.common.exception.ExceptionCode.MEMBERSHIP_CONFLICT;
import static com.withus.withmebe.common.exception.ExceptionCode.PAYMENT_CONFLICT;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.withus.withmebe.common.exception.CustomException;
import com.withus.withmebe.common.exception.ExceptionCode;
import com.withus.withmebe.member.entity.Member;
import com.withus.withmebe.member.repository.MemberRepository;
import com.withus.withmebe.member.type.Membership;
import com.withus.withmebe.payment.dto.request.ApprovePaymentRequest;
import com.withus.withmebe.payment.dto.request.ApproveRequestToKcp;
import com.withus.withmebe.payment.dto.response.AddPaymentResponse;
import com.withus.withmebe.payment.dto.response.PaymentResponse;
import com.withus.withmebe.payment.entity.Payment;
import com.withus.withmebe.payment.repository.PaymentRepository;
import com.withus.withmebe.payment.type.Currency;
import com.withus.withmebe.payment.type.PayType;
import com.withus.withmebe.payment.type.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class PaymentService {

  private final PaymentRepository paymentRepository;
  private final MemberRepository memberRepository;
  private final static String KCP_CERT_INFO
      = "-----BEGIN CERTIFICATE-----"
      + "MIIDgTCCAmmgAwIBAgIHBy4lYNG7ojANBgkqhkiG9w0BAQsFADBzMQswCQYDVQQGEwJLUjEOMAwGA1UECAwFU"
      + "2VvdWwxEDAOBgNVBAcMB0d1cm8tZ3UxFTATBgNVBAoMDE5ITktDUCBDb3JwLjETMBEGA1UECwwKSVQgQ2VudGV"
      + "yLjEWMBQGA1UEAwwNc3BsLmtjcC5jby5rcjAeFw0yMTA2MjkwMDM0MzdaFw0yNjA2MjgwMDM0MzdaMHAxCzAJB"
      + "gNVBAYTAktSMQ4wDAYDVQQIDAVTZW91bDEQMA4GA1UEBwwHR3Vyby1ndTERMA8GA1UECgwITG9jYWxXZWIxETA"
      + "PBgNVBAsMCERFVlBHV0VCMRkwFwYDVQQDDBAyMDIxMDYyOTEwMDAwMDI0MIIBIjANBgkqhkiG9w0BAQEFAAOCA"
      + "Q8AMIIBCgKCAQEAppkVQkU4SwNTYbIUaNDVhu2w1uvG4qip0U7h9n90cLfKymIRKDiebLhLIVFctuhTmgY7tkE"
      + "7yQTNkD+jXHYufQ/qj06ukwf1BtqUVru9mqa7ysU298B6l9v0Fv8h3ztTYvfHEBmpB6AoZDBChMEua7Or/L3C2"
      + "vYtU/6lWLjBT1xwXVLvNN/7XpQokuWq0rnjSRThcXrDpWMbqYYUt/CL7YHosfBazAXLoN5JvTd1O9C3FPxLxwcI"
      + "AI9H8SbWIQKhap7JeA/IUP1Vk4K/o3Yiytl6Aqh3U1egHfEdWNqwpaiHPuM/jsDkVzuS9FV4RCdcBEsRPnAWHz1"
      + "0w8CX7e7zdwIDAQABox0wGzAOBgNVHQ8BAf8EBAMCB4AwCQYDVR0TBAIwADANBgkqhkiG9w0BAQsFAAOCAQEAg9"
      + "lYy+dM/8Dnz4COc+XIjEwr4FeC9ExnWaaxH6GlWjJbB94O2L26arrjT2hGl9jUzwd+BdvTGdNCpEjOz3KEq8yJh"
      + "cu5mFxMskLnHNo1lg5qtydIID6eSgew3vm6d7b3O6pYd+NHdHQsuMw5S5z1m+0TbBQkb6A9RKE1md5/Yw+NymDy"
      + "+c4NaKsbxepw+HtSOnma/R7TErQ/8qVioIthEpwbqyjgIoGzgOdEFsF9mfkt/5k6rR0WX8xzcro5XSB3T+oecMS"
      + "54j0+nHyoS96/llRLqFDBUfWn5Cay7pJNWXCnw4jIiBsTBa3q95RVRyMEcDgPwugMXPXGBwNoMOOpuQ=="
      + "-----END CERTIFICATE-----";
  private final static String SITE_CD = "T0000";
  private final static long PREMIUM_MEMBERSHIP_PRICE = 10000L;
  private final static String PREMIUM_MEMBERSHIP_NAME = "프리미엄 멤버쉽";
  private final static String APPROVE_URL = "https://stg-spl.kcp.co.kr/gw/enc/v1/payment";

  @Transactional
  public AddPaymentResponse createPayment(long requesterId) {
    validateCreatePaymentRequest(requesterId);

    Payment newPayment = paymentRepository.save(Payment.builder()
        .payerId(requesterId)
        .goodName(PREMIUM_MEMBERSHIP_NAME)
        .goodPrice(PREMIUM_MEMBERSHIP_PRICE)
        .payType(PayType.PACA)
        .build());
    return newPayment.toAddPaymentResponse(SITE_CD, Currency.WON);
  }

  @Transactional
  public PaymentResponse approvePayment(long requesterId, ApprovePaymentRequest request) {
    Payment payment = readPayment(request.getOrdrId());
    validateApprovePaymentRequest(requesterId, request, payment);

    String tradeNo = requestApproveToKcp(
        payment.toApproveRequestToKcp(SITE_CD, KCP_CERT_INFO, request));
    payment.approve(tradeNo);
    changeMemberShip(requesterId);

    return payment.toPaymentResponse();
  }

  @Transactional
  public PaymentResponse cancelPayment(long requesterId, long paymentId) {
    Payment payment = readPayment(paymentId);
    validateCancelPaymentRequest(requesterId, payment);

    payment.cancel();
    changeMemberShip(requesterId);

    return payment.toPaymentResponse();
  }

  @Transactional(readOnly = true)
  public Page<PaymentResponse> readPayments(long requesterId, Pageable pageable) {

    return paymentRepository.findByMemberIdAndStatusIsNot(requesterId, Status.CREATED, pageable)
        .map(Payment::toPaymentResponse);
  }

  private String requestApproveToKcp(ApproveRequestToKcp request) {
    RestTemplate restTemplate = new RestTemplate();
    Gson gson = new Gson();

    ResponseEntity<String> responseEntity = restTemplate.postForEntity(APPROVE_URL,
        gson.toJson(request), String.class);

    if (responseEntity.getStatusCode() != HttpStatusCode.valueOf(200)) {
      throw new CustomException(FAIL_TO_REQUEST_APPROVE_PAYMENT);
    }

    return JsonParser.parseString(responseEntity.getBody())
        .getAsJsonObject().get("tno")
        .getAsString();
  }

  private void changeMemberShip(long requesterId) {
    Member requester = readMember(requesterId);
    if (requester.isPremiumMember()) {
      requester.setMembership(Membership.FREE);
    } else {
      requester.setMembership(Membership.PREMIUM);
    }
  }

  private void validateApprovePaymentRequest(long requesterId, ApprovePaymentRequest request,
      Payment payment) {
    if (!payment.isStatus(Status.CREATED)) {
      throw new CustomException(PAYMENT_CONFLICT);
    }
    if (!payment.isPayer(requesterId)) {
      throw new CustomException(AUTHORIZATION_ISSUE);
    }
    if (!payment.isGoodPrice(request)) {
      throw new CustomException(PAYMENT_CONFLICT);
    }
  }

  private void validateCreatePaymentRequest(long requesterId) {
    if (readMember(requesterId).isPremiumMember()) {
      throw new CustomException(MEMBERSHIP_CONFLICT);
    }
  }

  private void validateCancelPaymentRequest(long requesterId, Payment payment) {
    if (!payment.isStatus(Status.APPROVED)) {
      throw new CustomException(PAYMENT_CONFLICT);
    }
    if (!payment.isPayer(requesterId)) {
      throw new CustomException(AUTHORIZATION_ISSUE);
    }
  }

  private Member readMember(long requesterId) {
    return memberRepository.findById(requesterId)
        .orElseThrow(() -> new CustomException(ExceptionCode.ENTITY_NOT_FOUND));
  }

  private Payment readPayment(long paymentId) {
    return paymentRepository.findById(paymentId)
        .orElseThrow(() -> new CustomException(ExceptionCode.ENTITY_NOT_FOUND));
  }

}
