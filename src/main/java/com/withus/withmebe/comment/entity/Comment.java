package com.withus.withmebe.comment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EntityListeners(value = AuditingEntityListener.class)
public final class Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "comment_id")
  private Long id;

  private Long gatheringId;
  private Long memberId;

  private String content;

  @CreatedDate
  @Column(updatable = false)
  private LocalDateTime createdDttm;

  @LastModifiedDate
  private LocalDateTime modifiedDttm;

  private LocalDateTime deletedDttm;

  public void delete() {
    deletedDttm = LocalDateTime.now();
  }

  public void setContent(String content) {
    this.content = String.valueOf(content);
  }
}
