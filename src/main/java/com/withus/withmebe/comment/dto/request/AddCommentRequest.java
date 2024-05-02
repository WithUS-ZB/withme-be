package com.withus.withmebe.comment.dto.request;

import com.withus.withmebe.comment.entity.Comment;
import com.withus.withmebe.member.entity.Member;

public record AddCommentRequest(
    String commentContent
) {

  public Comment toEntity(Long gatheringId, Member member) {
    return Comment.builder()
        .gatheringId(gatheringId)
        .member(member)
        .commentContent(this.commentContent)
        .build();
  }
}
