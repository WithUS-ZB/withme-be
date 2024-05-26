package com.withus.withmebe.gathering.Type.converter;

import com.withus.withmebe.gathering.Type.GatheringType;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;

public class GatheringTypeConverter implements Converter<String, GatheringType> {

  @Override
  public GatheringType convert(@NotNull String request) {
    return GatheringType.of(request);
  }
}
