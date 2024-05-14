package com.withus.withmebe.payment.repository;

import com.withus.withmebe.payment.entity.Payment;
import com.withus.withmebe.payment.type.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
  Page<Payment> findByMemberIdAndStatusIsNot(Long memberId, Status status, Pageable pageable);
}
