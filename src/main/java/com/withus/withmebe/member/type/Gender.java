package com.withus.withmebe.member.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Gender {
  MALE("남성"),
  FEMALE("여성"),
  NONE("없음");

  private final String value;
}