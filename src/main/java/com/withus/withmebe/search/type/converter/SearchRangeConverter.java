package com.withus.withmebe.search.type.converter;

import com.withus.withmebe.search.type.SearchRange;
import org.springframework.core.convert.converter.Converter;

public class SearchRangeConverter implements Converter<String, SearchRange> {

  @Override
  public SearchRange convert(String request) {
    return SearchRange.of(request);
  }
}
