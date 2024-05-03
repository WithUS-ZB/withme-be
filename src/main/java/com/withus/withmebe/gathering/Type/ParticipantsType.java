package com.withus.withmebe.gathering.Type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ParticipantsType {
    MINOR("19세미만"),
    ADULT("19세이상"),
    NO_RESTRICTIONS("제한없음");

    private final String value;
}
