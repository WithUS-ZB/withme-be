package com.withus.withmebe.participation.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Status {

  CREATED("승인대기"),
  APPROVED("승인"),
  REJECTED("거절"),
  CANCELED("취소");

  private final String value;
}
