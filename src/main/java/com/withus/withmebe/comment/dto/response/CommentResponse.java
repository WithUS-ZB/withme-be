package com.withus.withmebe.comment.dto.response;

import com.withus.withmebe.comment.entity.Comment;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public final class CommentResponse {

  private Long id;
  private String nickName;
  private String content;
  private LocalDateTime createdDttm;
  private LocalDateTime modifiedDttm;
  private LocalDateTime deletedDttm;

  public static CommentResponse fromEntity(Comment comment, String nickName) {
    return CommentResponse.builder()
        .id(comment.getId())
        .nickName(nickName)
        .content(comment.getContent())
        .createdDttm(comment.getCreatedDttm())
        .modifiedDttm(comment.getModifiedDttm())
        .deletedDttm(comment.getDeletedDttm())
        .build();
  }
}
