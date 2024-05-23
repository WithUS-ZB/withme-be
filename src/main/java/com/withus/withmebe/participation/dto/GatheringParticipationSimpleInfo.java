package com.withus.withmebe.participation.dto;

import com.withus.withmebe.member.type.Gender;
import com.withus.withmebe.participation.type.Status;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record GatheringParticipationSimpleInfo(
    Long id,
    String nickName,
    LocalDate birthDate,
    Gender gender,
    String profileImg,
    Status status,
    LocalDateTime createdDttm,
    LocalDateTime updatedDttm

) {

}
