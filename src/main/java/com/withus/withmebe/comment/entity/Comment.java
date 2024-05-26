package com.withus.withmebe.comment.entity;

import com.withus.withmebe.comment.dto.request.SetCommentRequest;
import com.withus.withmebe.comment.dto.response.CommentResponse;
import com.withus.withmebe.common.entity.BaseEntity;
import com.withus.withmebe.member.entity.Member;
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
@SQLDelete(sql = "UPDATE comment SET deleted_dttm = CURRENT_TIMESTAMP, updated_dttm = CURRENT_TIMESTAMP WHERE comment_id = ?")
public class Comment extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "comment_id")
  private Long id;

  @Column(nullable = false)
  private Long gatheringId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id", nullable = false)
  private Member writer;

  @Column(nullable = false)
  private String commentContent;

  @Builder
  public Comment(Long gatheringId, Member writer, String commentContent) {
    this.gatheringId = gatheringId;
    this.writer = writer;
    this.commentContent = commentContent;
  }

  public CommentResponse toResponse() {
    return CommentResponse.builder()
        .id(this.id)
        .nickName(this.writer.getNickName())
        .profileImg(this.writer.getProfileImg())
        .commentContent(this.commentContent)
        .createdDttm(this.getCreatedDttm())
        .updatedDttm(this.getUpdatedDttm())
        .build();
  }

  public boolean isWriter(long memberId) {
    return this.writer.getId() == memberId;
  }

  public void updateComment(SetCommentRequest request) {
    this.commentContent = request.commentContent();
  }
}
