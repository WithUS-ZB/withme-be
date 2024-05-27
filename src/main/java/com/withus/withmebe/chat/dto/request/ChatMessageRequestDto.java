package com.withus.withmebe.chat.dto.request;

public record ChatMessageRequestDto(
    Long chatroomId,
    String content
) {

}
