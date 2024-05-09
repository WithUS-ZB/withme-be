package com.withus.withmebe.participation.dto;

import com.withus.withmebe.participation.type.Status;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ParticipationSimpleInfo(
    Long id,
    String nickName,
    Status status,
    LocalDateTime updatedDttm

) {

}
