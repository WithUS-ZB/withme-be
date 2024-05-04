package com.withus.withmebe.comment.dto.request;

import com.withus.withmebe.comment.entity.Comment;
import com.withus.withmebe.member.entity.Member;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record AddCommentRequest(
    @NotBlank
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
