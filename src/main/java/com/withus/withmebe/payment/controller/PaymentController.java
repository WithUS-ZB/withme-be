package com.withus.withmebe.payment.controller;

import com.withus.withmebe.payment.dto.response.AddPaymentResponse;
import com.withus.withmebe.payment.service.PaymentService;
import com.withus.withmebe.security.anotation.CurrentMemberId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class PaymentController {

  private final PaymentService paymentService;

  @PostMapping
  public ResponseEntity<AddPaymentResponse> addPayment(@CurrentMemberId long currentMemberId) {
    return ResponseEntity.ok(paymentService.createPayment(currentMemberId));
  }
}
