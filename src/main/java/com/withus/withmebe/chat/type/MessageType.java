package com.withus.withmebe.chat.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MessageType {
  CHAT("일반"),
  JOIN("채팅방입장"),
  LEAVE("채팅방퇴장"),
  FILE("파일"),
  NOTIFICATION("공지");

  @Getter
  private final String value;
}