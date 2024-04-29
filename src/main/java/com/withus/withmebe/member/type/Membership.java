package com.withus.withmebe.member.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Membership {
  FREE("무료"),
  PREMIUM("프리미엄");

  private final String value;
}
