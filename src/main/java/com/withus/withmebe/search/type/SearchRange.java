package com.withus.withmebe.search.type;

import com.withus.withmebe.gathering.Type.GatheringType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SearchRange implements Option{
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

  @Override
  public String getField() {
    return "gathering_type";
  }

  @Override
  public String getName() {
    return this.toString();
  }

  @Override
  public String getValue() {
    return value;
  }
}
