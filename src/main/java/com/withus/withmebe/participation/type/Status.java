package com.withus.withmebe.participation.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Status {

  CREATED("승인대기"),
  APPROVED("승인"),
  REJECTED("거절"),
  CANCELED("취소"),
  CHAT_JOINED("채팅참여"),
  CHAT_LEFT("채팅나감");

  private final String value;
}
