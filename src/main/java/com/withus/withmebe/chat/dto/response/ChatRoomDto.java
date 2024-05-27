package com.withus.withmebe.chat.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ChatRoomDto(
    Long chatroomId,

    Long gatheringId,

    // 채팅 제목
    String title,

    // 모임 날짜 시간
    LocalDateTime gatheringDttm,

    // 마지막 채팅 내용
    String lastMessageContent,

    // 마지막 채팅 날짜 시간
    LocalDateTime lastMessageDttm,

    // 현재 채팅 참여 인원
    Long memberCount
) {}
