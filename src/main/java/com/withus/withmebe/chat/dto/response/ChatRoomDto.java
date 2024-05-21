package com.withus.withmebe.chat.dto.response;

import lombok.Builder;

@Builder
public record ChatRoomDto(
    Long chatId,

    String title
) {}
