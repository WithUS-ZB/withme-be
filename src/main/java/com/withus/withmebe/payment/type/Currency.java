package com.withus.withmebe.payment.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Currency {

  WON("원화", "410");

  private final String value;
  private final String code;
}
