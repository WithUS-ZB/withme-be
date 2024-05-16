package com.withus.withmebe.festival.dto;

import lombok.Builder;

@Builder
public record FestivalSimpleInfo(
    String title,
    String img,
    String link
) {

}
