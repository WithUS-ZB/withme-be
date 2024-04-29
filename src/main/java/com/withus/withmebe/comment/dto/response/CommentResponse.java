package com.withus.withmebe.comment.dto.response;

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
}
