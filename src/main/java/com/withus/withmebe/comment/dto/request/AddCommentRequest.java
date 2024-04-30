package com.withus.withmebe.comment.dto.request;

import com.withus.withmebe.comment.entity.Comment;

public record AddCommentRequest(
    String commentContent
) {

  public Comment toEntity(Long gatheringId, Long memberId) {
    return Comment.builder()
        .gatheringId(gatheringId)
        .memberId(memberId)
        .commentContent(this.commentContent)
        .build();
  }
}
