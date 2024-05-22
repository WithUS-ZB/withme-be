package com.withus.withmebe.search.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Builder;

@Builder
public record GatheringSearchResponse(
    Long gatheringId,
    Long memberId,
    String nickName,
    String profileImg,
    String title,
    String content,
    String gatheringType,
    Long maximumParticipant,
    LocalDate day,
    LocalTime time,
    LocalDateTime recruitmentStartDt,
    LocalDateTime recruitmentEndDt,
    String category,
    String address,
    String mainImg,
    String participantsType,
    Long fee,
    String participantSelectionMethod,
    Long likeCount,
    LocalDateTime createdDttm,
    String status
)
{

}
