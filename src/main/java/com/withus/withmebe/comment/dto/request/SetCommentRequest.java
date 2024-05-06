package com.withus.withmebe.comment.dto.request;

import javax.validation.constraints.NotBlank;

public record SetCommentRequest(
    @NotBlank
    String commentContent
) {

}
