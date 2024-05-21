package com.withus.withmebe.subscription.controller;

import com.withus.withmebe.security.anotation.CurrentMemberId;
import com.withus.withmebe.subscription.dto.SubscriptionSimpleInfo;
import com.withus.withmebe.subscription.dto.SubscriptionResponse;
import com.withus.withmebe.subscription.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subscription")
public class SubscriptionController {

  private final SubscriptionService subscriptionService;

  @PostMapping
  public ResponseEntity<SubscriptionResponse> addSubscription(
      @CurrentMemberId long currentMemberId,
      @RequestParam("providerid") long providerId) {
    return ResponseEntity.ok(subscriptionService.createSubscription(currentMemberId, providerId));
  }

  @GetMapping
  public ResponseEntity<Boolean> getIsSubscribed(
      @CurrentMemberId long currentMemberId,
      @RequestParam("providerid") long providerId) {
    return ResponseEntity.ok(subscriptionService.isSubscribed(currentMemberId, providerId));
  }

  @DeleteMapping
  public ResponseEntity<Boolean> deleteSubscription(
      @CurrentMemberId long currentMemberId,
      @RequestParam("providerid") long providerId) {
    return ResponseEntity.ok(subscriptionService.deleteSubscription(currentMemberId, providerId));
  }

  @GetMapping("/list")
  public ResponseEntity<Page<SubscriptionSimpleInfo>> getProviders(
      @CurrentMemberId long currentMemberId,
      @PageableDefault Pageable pageable) {
    return ResponseEntity.ok(subscriptionService.readProviders(currentMemberId, pageable));
  }

  @GetMapping("/subscriber/list")
  public ResponseEntity<Page<SubscriptionSimpleInfo>> getSubscribers(
      @CurrentMemberId long currentMemberId,
      @PageableDefault Pageable pageable) {
    return ResponseEntity.ok(subscriptionService.readSubscribers(currentMemberId, pageable));
  }
}
