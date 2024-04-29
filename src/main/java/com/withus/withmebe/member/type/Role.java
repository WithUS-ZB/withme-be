package com.withus.withmebe.member.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role {
  ROLE_ADMIN("관리자"),
  ROLE_MEMBER("회원");

  private final String value;
}
