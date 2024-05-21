package com.withus.withmebe.subscription.entity;

import com.withus.withmebe.common.entity.BaseEntity;
import com.withus.withmebe.member.entity.Member;
import com.withus.withmebe.subscription.dto.SubscriptionSimpleInfo;
import com.withus.withmebe.subscription.dto.SubscriptionResponse;
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
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@NoArgsConstructor
@Getter
@Where(clause = "deleted_dttm is null")
@SQLDelete(sql = "UPDATE subscription SET deleted_dttm = NOW() WHERE subscription_id = ?")
public class Subscription extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "subscription_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "subscriber_id", nullable = false)
  private Member subscriber;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "provider_id", nullable = false)
  private Member provider;

  @Builder
  public Subscription(Member subscriber, Member provider) {
    this.subscriber = subscriber;
    this.provider = provider;
  }

  public SubscriptionResponse toResponse() {
    return SubscriptionResponse.builder()
        .id(this.id)
        .subscriberId(this.subscriber.getId())
        .providerId(this.provider.getId())
        .createdDttm(this.getCreatedDttm())
        .build();
  }

  public SubscriptionSimpleInfo toProviderInfo() {
    return SubscriptionSimpleInfo.builder()
        .memberId(this.provider.getId())
        .profileImg(this.provider.getProfileImg())
        .nickName(this.provider.getNickName())
        .createdDttm(this.getCreatedDttm())
        .build();
  }

  public SubscriptionSimpleInfo toSubscriberInfo() {
    return SubscriptionSimpleInfo.builder()
        .memberId(this.subscriber.getId())
        .profileImg(this.subscriber.getProfileImg())
        .nickName(this.subscriber.getNickName())
        .createdDttm(this.getCreatedDttm())
        .build();
  }
}
