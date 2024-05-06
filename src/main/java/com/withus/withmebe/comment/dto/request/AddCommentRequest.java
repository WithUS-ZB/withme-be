package com.withus.withmebe.comment.dto.request;

import com.withus.withmebe.comment.entity.Comment;
import com.withus.withmebe.member.entity.Member;
import javax.validation.constraints.NotBlank;

public record AddCommentRequest(
    @NotBlank
    String commentContent
) {

  public Comment toEntity(Long gatheringId, Member requester) {
    return Comment.builder()
        .gatheringId(gatheringId)
        .writer(requester)
        .commentContent(this.commentContent)
        .build();
  }
}
