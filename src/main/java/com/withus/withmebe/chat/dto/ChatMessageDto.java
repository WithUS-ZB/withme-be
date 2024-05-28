package com.withus.withmebe.chat.dto;

import com.withus.withmebe.chat.type.MessageType;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ChatMessageDto(
    Long chatId,
    String content,
    Long memberId,
    String profileImg,
    String nickName,
    MessageType messageType,
    LocalDateTime chatDateTime
) {

}
