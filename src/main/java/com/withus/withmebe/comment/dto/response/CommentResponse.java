package com.withus.withmebe.comment.dto.response;

import com.withus.withmebe.comment.entity.Comment;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {

  long id;
  String nickName;
  String content;
  LocalDateTime createdDttm;
  LocalDateTime modifiedDttm;
  LocalDateTime deletedDttm;

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
