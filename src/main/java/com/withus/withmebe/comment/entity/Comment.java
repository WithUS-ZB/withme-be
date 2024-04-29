package com.withus.withmebe.comment.entity;

import com.withus.withmebe.comment.dto.request.AddCommentRequest;
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
public class Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "comment_id")
  long id;

  long gatheringId;
  long memberId;

  String content;

  @CreatedDate
  LocalDateTime createdDttm;
  @LastModifiedDate
  LocalDateTime modifiedDttm;
  LocalDateTime deletedDttm;

  public static Comment fromRequest(long gatheringId, long memberId, AddCommentRequest request) {
    return Comment.builder()
        .gatheringId(gatheringId)
        .memberId(memberId)
        .content(request.getContent())
        .build();
  }
}
