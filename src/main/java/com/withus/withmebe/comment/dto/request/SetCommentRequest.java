package com.withus.withmebe.comment.dto.request;

import jakarta.validation.constraints.NotNull;

public record SetCommentRequest(
    @NotNull
    String commentContent
) {

}
