package com.withus.withmebe.participation.dto;

import com.withus.withmebe.participation.type.Status;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record MyParticipationSimpleInfo(
    Long id,
    String title,
    Status status,
    LocalDateTime updatedDttm

) {

}
