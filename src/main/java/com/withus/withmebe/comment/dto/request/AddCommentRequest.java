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
public final class AddCommentRequest {

  private String content;

  public Comment toEntity(Long gatheringId, Long memberId) {
    return Comment.builder()
        .gatheringId(gatheringId)
        .memberId(memberId)
        .content(this.content)
        .build();
  }
}
