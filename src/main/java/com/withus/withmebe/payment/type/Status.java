package com.withus.withmebe.payment.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Status {
  CREATED("인증대기"),
  APPROVED("승인");

  private final String value;
}
