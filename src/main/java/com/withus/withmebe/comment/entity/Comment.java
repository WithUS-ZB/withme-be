package com.withus.withmebe.comment.entity;

import com.withus.withmebe.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@NoArgsConstructor
@Getter
@EntityListeners(value = AuditingEntityListener.class)
public class Comment extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "comment_id")
  private Long id;

  private Long gatheringId;
  private Long memberId;

  private String commentContent;

  public void setCommentContent(String commentContent) {
    this.commentContent = commentContent;
  }

  @Builder
  public Comment(Long gatheringId, Long memberId, String commentContent) {
    this.gatheringId = gatheringId;
    this.memberId = memberId;
    this.commentContent = commentContent;
  }
}
