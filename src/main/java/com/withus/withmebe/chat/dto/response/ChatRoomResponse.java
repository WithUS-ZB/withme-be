package com.withus.withmebe.chat.dto.response;

import lombok.Builder;

@Builder
public record ChatRoomResponse(
    Long chatId,

    String title
) {}
