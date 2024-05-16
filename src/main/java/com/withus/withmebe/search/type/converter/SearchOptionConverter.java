package com.withus.withmebe.search.type.converter;

import com.withus.withmebe.search.type.SearchOption;
import org.springframework.core.convert.converter.Converter;

public class SearchOptionConverter implements Converter<String, SearchOption> {

  @Override
  public SearchOption convert(String requestParam) {
    return SearchOption.of(requestParam);
  }
}
