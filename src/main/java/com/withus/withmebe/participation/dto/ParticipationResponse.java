package com.withus.withmebe.participation.dto;

import com.withus.withmebe.participation.type.Status;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ParticipationResponse(
    Long id,
    String nickName,
    String title,
    Status status,
    LocalDateTime createdDttm,
    LocalDateTime updatedDttm
) {

}
