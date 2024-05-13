package com.withus.withmebe.payment.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PayMethod {
  PACA("신용카드"),
  PABK("계좌이체"),
  PAVC("가상계좌"),
  PAMC("휴대폰"),
  PAPT("포인트"),
  PATK("상품권"),
  ;


  private final String value;
}
