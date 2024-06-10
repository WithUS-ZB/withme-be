package com.withus.withmebe.participation.type;

import com.withus.withmebe.participation.status.ApproveStatusChanger;
import com.withus.withmebe.participation.status.CanceledStatusChanger;
import com.withus.withmebe.participation.status.CreatedStatusChanger;
import com.withus.withmebe.participation.status.JoinChatStatusChanger;
import com.withus.withmebe.participation.status.LeaveChatStatusChanger;
import com.withus.withmebe.participation.status.ParticipationStatusChangeable;
import com.withus.withmebe.participation.status.RejectStatusChanger;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Status {

  CREATED("승인대기", CreatedStatusChanger.class),
  APPROVED("승인", ApproveStatusChanger.class),
  REJECTED("거절", RejectStatusChanger.class),
  CANCELED("취소", CanceledStatusChanger.class),
  CHAT_JOINED("채팅참여", JoinChatStatusChanger.class),
  CHAT_LEFT("채팅나감", LeaveChatStatusChanger.class);

  private final String value;
  private final Class<? extends ParticipationStatusChangeable> statusChangerClass;
}
