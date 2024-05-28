package com.withus.withmebe.common.config;

import com.withus.withmebe.gathering.Type.converter.GatheringTypeConverter;
import com.withus.withmebe.search.type.converter.SearchOptionConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  @Override
  public void addFormatters(FormatterRegistry registry) {
    registry.addConverter(new SearchOptionConverter());
    registry.addConverter(new GatheringTypeConverter());
  }
}
