package com.withus.withmebe.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass // 해당 클래스의 필드와 매핑 정보는 하위 엔티티 클래스에 상속
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

  @Column(nullable = false, updatable = false)
  @CreatedDate
  private LocalDateTime createdDttm;

  @LastModifiedDate
  private LocalDateTime updatedDttm;

  private LocalDateTime deletedDttm;
}