package com.withus.withmebe.member.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SignupPath {
  NORMAL("일반회원가입"),
  KAKAO("kakao");

  @Getter
  private final String value;
}
