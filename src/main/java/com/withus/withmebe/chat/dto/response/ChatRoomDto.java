package com.withus.withmebe.chat.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ChatRoomDto(
    Long chatId,

    Long gatheringId,

    // 채팅 제목
    String title,

    // 모임 날짜
    LocalDateTime localDateTime
) {}
