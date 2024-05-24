package com.withus.withmebe.gathering.Type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum GatheringType {
    ALL("all", "전체"),
    MEETING("meeting","미팅"),
    EVENT("event","이벤트");

    private final String request;
    private final String value;

    public static GatheringType of(String request) {
        for (GatheringType type : GatheringType.values()) {
            if (type.request.equals(request)) {
                return type;
            }
        }
        throw new IllegalArgumentException();
    }
}
