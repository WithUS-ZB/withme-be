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
  String nick_Name;
  String content;
  LocalDateTime createdDttm;
  LocalDateTime modifiedDttm;

  public static CommentResponse fromEntity(Comment comment, String name) {
    return CommentResponse.builder()
        .id(comment.getId())
        .nick_Name(name)
        .content(comment.getContent())
        .createdDttm(comment.getCreatedDttm())
        .modifiedDttm(comment.getModifiedDttm())
        .build();
  }
}
