package com.withus.withmebe.gathering.dto.response;

import com.withus.withmebe.gathering.Type.GatheringType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Builder;

@Builder
public record LikedGatheringSimpleInfo (
    Long id,
    Long gatheringId,
    String title,
    String mainImg,
    LocalDate day,
    LocalTime time,
    GatheringType gatheringType,
    Long likeCount,
    LocalDateTime updatedDttm
)
{

}
