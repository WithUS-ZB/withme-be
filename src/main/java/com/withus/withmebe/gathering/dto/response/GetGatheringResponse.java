package com.withus.withmebe.gathering.dto.response;

import com.withus.withmebe.gathering.Type.GatheringType;
import com.withus.withmebe.gathering.Type.ParticipantSelectionMethod;
import com.withus.withmebe.gathering.Type.ParticipantsType;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetGatheringResponse {
    private Long memberId;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotNull
    private GatheringType gatheringType;

    private Long maximumParticipant;

    @NotNull
    private LocalDateTime startDttm;

    @NotNull
    private LocalDateTime endDttm;

    @NotBlank
    private String category;

    @NotNull
    private LocalDateTime applicationDeadLine;

    @NotBlank
    private String address;

    @NotBlank
    private String detailedAddress;

    @NotBlank
    private String location;

    @NotBlank
    private String mainImg;

    @NotNull
    private ParticipantsType participantsType;

    @NotNull
    private Long fee;

    @NotNull
    private ParticipantSelectionMethod participantSelectionMethod;
}
