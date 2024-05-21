package com.withus.withmebe.subscription.service;

import static com.withus.withmebe.common.exception.ExceptionCode.ENTITY_NOT_FOUND;

import com.withus.withmebe.common.exception.CustomException;
import com.withus.withmebe.common.exception.ExceptionCode;
import com.withus.withmebe.member.entity.Member;
import com.withus.withmebe.member.repository.MemberRepository;
import com.withus.withmebe.subscription.dto.SubscriptionSimpleInfo;
import com.withus.withmebe.subscription.dto.SubscriptionResponse;
import com.withus.withmebe.subscription.entity.Subscription;
import com.withus.withmebe.subscription.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

  private final SubscriptionRepository subscriptionRepository;
  private final MemberRepository memberRepository;

  @Transactional
  public SubscriptionResponse createSubscription(long requesterId, long providerId) {

    validateCreateSubscriptionRequest(requesterId, providerId);

    Subscription subscription = subscriptionRepository.save(Subscription.builder()
        .subscriber(readMember(requesterId))
        .provider(readMember(providerId))
        .build());
    return subscription.toResponse();
  }

  @Transactional(readOnly = true)
  public boolean isSubscribed(long requesterId, long providerId) {
    return subscriptionRepository.existsBySubscriber_IdAndProvider_Id(requesterId, providerId);
  }

  @Transactional(readOnly = true)
  public Page<SubscriptionSimpleInfo> readProviders(long requesterId, Pageable pageable) {
    return subscriptionRepository.findBySubscriber_Id(requesterId, pageable)
        .map(Subscription::toProviderInfo);
  }

  @Transactional(readOnly = true)
  public Page<SubscriptionSimpleInfo> readSubscribers(long requesterId, Pageable pageable) {
    return subscriptionRepository.findByProvider_Id(requesterId, pageable)
        .map(Subscription::toSubscriberInfo);
  }

  public boolean deleteSubscription(long requesterId, long providerId) {

    Subscription subscription =
        subscriptionRepository.findBySubscriber_IdAndProvider_Id(requesterId, providerId)
            .orElseThrow(() -> new CustomException(ENTITY_NOT_FOUND));

    subscriptionRepository.delete(subscription);
    return true;
  }

  private void validateCreateSubscriptionRequest(long requesterId, long providerId) {
    validateRequesterIsNotProvider(requesterId, providerId);
    validateSubscriptionIsNotExists(requesterId, providerId);
  }

  private void validateRequesterIsNotProvider(long requesterId, long providerId) {
    if (requesterId == providerId) {
      throw new CustomException(ExceptionCode.AUTHORIZATION_ISSUE);
    }
  }

  private void validateSubscriptionIsNotExists(long requesterId, long providerId) {
    if (isSubscribed(requesterId, providerId)) {
      throw new CustomException(ExceptionCode.SUBSCRIPTION_DUPLICATED);
    }
  }

  private Member readMember(long memberId) {
    return memberRepository.findById(memberId)
        .orElseThrow(() -> new CustomException(ENTITY_NOT_FOUND));
  }
}
