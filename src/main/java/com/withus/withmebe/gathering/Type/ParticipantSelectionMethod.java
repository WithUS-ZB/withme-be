package com.withus.withmebe.gathering.Type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ParticipantSelectionMethod {
    FIRST_COME("선착순"),
    UNLIMITED_APPLICATION("제한없음");

    private final String value;
}
