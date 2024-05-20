package com.withus.withmebe.subscription.repository;

import com.withus.withmebe.subscription.entity.Subscription;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {


  boolean existsBySubscriber_IdAndProvider_Id(Long subscriberId, Long providerId);
  Optional<Subscription> findBySubscriber_IdAndProvider_Id(Long subscriberId, Long providerId);
  List<Subscription> findAllBySubscriber_Id(Long subscriberId);

  @EntityGraph(attributePaths = "provider")
  Page<Subscription> findBySubscriber_Id(Long subscriberId, Pageable pageable);
  @EntityGraph(attributePaths = "subscriber")
  Page<Subscription> findByProvider_Id(Long providerId, Pageable pageable);

}
