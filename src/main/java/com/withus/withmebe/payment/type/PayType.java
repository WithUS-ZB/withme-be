package com.withus.withmebe.payment.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PayType {
  PACA("신용카드", "100000000000");


  private final String value;
  private final String code;
}
