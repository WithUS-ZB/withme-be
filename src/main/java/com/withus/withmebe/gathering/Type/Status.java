package com.withus.withmebe.gathering.Type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Status {
    PROGRESS("진행중"),
    CANCELED("취소");

    private final String value;
}
