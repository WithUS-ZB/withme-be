package com.withus.withmebe.comment.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record SetCommentRequest(
    @NotBlank
    String commentContent
) {

}
