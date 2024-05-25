package com.withus.withmebe.payment.controller;

import com.withus.withmebe.payment.dto.request.ApprovePaymentRequest;
import com.withus.withmebe.payment.dto.response.AddPaymentResponse;
import com.withus.withmebe.payment.dto.response.PaymentResponse;
import com.withus.withmebe.payment.service.PaymentService;
import com.withus.withmebe.security.anotation.CurrentMemberId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

  @PutMapping("/approve")
  public ResponseEntity<PaymentResponse> approvePayment(
      @CurrentMemberId long currentMemberId,
      @RequestBody @Valid ApprovePaymentRequest request
  ) {
    return ResponseEntity.ok(paymentService.approvePayment(currentMemberId, request));
  }

  @PutMapping("/cancel/{paymentId}")
  public ResponseEntity<PaymentResponse> cancelPayment(
      @CurrentMemberId long currentMemberId, @PathVariable long paymentId) {
    return ResponseEntity.ok(paymentService.cancelPayment(currentMemberId, paymentId));
  }

  @GetMapping("/list")
  public ResponseEntity<Page<PaymentResponse>> getPayments(
      @CurrentMemberId long currentMemberId,
      @PageableDefault Pageable pageable) {
    return ResponseEntity.ok(paymentService.readPayments(currentMemberId, pageable));
  }
}
