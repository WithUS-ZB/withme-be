package com.withus.withmebe.comment.dto.request;

import com.withus.withmebe.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public final class SetCommentRequest {

  private String content;

  public Comment toEntity(Comment comment) {
    return Comment.builder()
        .id(comment.getId())
        .gatheringId(comment.getGatheringId())
        .memberId(comment.getMemberId())
        .content(this.content)
        .createdDttm(comment.getCreatedDttm())
        .build();
  }
}
