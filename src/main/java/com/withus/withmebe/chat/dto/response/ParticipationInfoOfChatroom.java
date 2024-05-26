package com.withus.withmebe.chat.dto.response;

import com.withus.withmebe.participation.type.Status;
import lombok.Builder;

@Builder
public record ParticipationInfoOfChatroom(
    Long participationId,
    Long chatroomId,
    Long memberId,
    String nickname,
    String chatroomTitle,
    Status participationStatus
) {

}
