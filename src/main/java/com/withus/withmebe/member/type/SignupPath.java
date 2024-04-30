package com.withus.withmebe.member.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SignupPath {
  NORMAL("일반회원가입"),
  KAKAO("카카오");

  private final String value;
}
