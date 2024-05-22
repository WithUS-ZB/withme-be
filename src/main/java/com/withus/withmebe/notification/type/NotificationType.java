package com.withus.withmebe.notification.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum NotificationType {

  GATHERING("모임 알림"),
  SUBSCRIPTION("구독 알림");

  private final String value;
}
