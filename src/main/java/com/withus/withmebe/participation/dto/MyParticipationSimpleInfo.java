package com.withus.withmebe.participation.dto;

import com.withus.withmebe.gathering.Type.GatheringType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Builder;

@Builder
public record MyParticipationSimpleInfo(
    Long id,
    Long gatheringId,
    String title,
    String mainImg,
    LocalDate day,
    LocalTime time,
    GatheringType gatheringType,
    com.withus.withmebe.gathering.Type.Status gatheringStatus,
    com.withus.withmebe.participation.type.Status status,
    LocalDateTime updatedDttm

) {

}
