package com.withus.withmebe.comment.dto.response;

import com.withus.withmebe.comment.entity.Comment;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record CommentResponse(
    Long id,
    String nickName,
    String commentContent,
    LocalDateTime createdDttm,
    LocalDateTime updatedDttm
) {

}
