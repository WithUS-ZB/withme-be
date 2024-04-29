package com.withus.withmebe.comment.dto.request;

import com.withus.withmebe.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddCommentRequest {

  String content;

  public Comment toEntity(long gatheringId, long memberId) {
    return Comment.builder()
        .gatheringId(gatheringId)
        .memberId(memberId)
        .content(this.content)
        .build();
  }
}
