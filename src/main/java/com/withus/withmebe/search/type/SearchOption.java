package com.withus.withmebe.search.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SearchOption {

  ALL("all", "all", "*"),
  FIRST("first", "participant_selection_method", "FIRST_COME"),
  APPLICATION("application", "participant_selection_method", "UNLIMITED_APPLICATION"),
  ADULT("adult", "participants_type", "ADULT"),
  MINOR("minor", "participants_type", "MINOR"),
  NO_LIMIT("no_limit", "participants_type", "NO_RESTRICTIONS"),
  PAY_FREE("pay_free", "fee", "0"),
  PAY_HAS("pay_has", "fee", "0"),
  EVENT("event", "gathering_type", "EVENT"),
  MEETING("meeting", "gathering_type", "MEETING");

  private final String request;
  private final String field;
  private final String value;

  public static SearchOption of(String request) {
    for (SearchOption option : SearchOption.values()) {
      if (option.request.equals(request)) {
        return option;
      }
    }
    throw new IllegalArgumentException();
  }
}
