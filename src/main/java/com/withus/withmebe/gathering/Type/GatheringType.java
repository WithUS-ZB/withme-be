package com.withus.withmebe.gathering.Type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum GatheringType {
    MEETING("미팅"),
    EVENT("이벤트");

    private final String value;
}
