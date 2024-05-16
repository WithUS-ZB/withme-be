package com.withus.withmebe.search.type;

import com.withus.withmebe.gathering.Type.GatheringType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SearchRange {
  ALL("all", "*"),
  EVENT("event", "EVENT"),
  MEETING("meeting", "MEETING"),
  ;

  private final String request;
  private final String value;
  public static SearchRange of(String request) {
    for (SearchRange range : SearchRange.values()) {
      if (range.request.equals(request)) {
        return range;
      }
    }
    throw new IllegalArgumentException();
  }
}
