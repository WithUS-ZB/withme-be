package com.withus.withmebe.gathering.Type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GatheringType {
    ALL("all", "전체"),
    MEETING("meeting","모임"),
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
