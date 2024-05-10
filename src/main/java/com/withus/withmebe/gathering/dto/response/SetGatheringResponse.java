package com.withus.withmebe.gathering.dto.response;

import com.withus.withmebe.gathering.Type.GatheringType;
import com.withus.withmebe.gathering.Type.ParticipantSelectionMethod;
import com.withus.withmebe.gathering.Type.ParticipantsType;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SetGatheringResponse {
    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotNull
    private GatheringType gatheringType;

    private Long maximumParticipant;

    @NotNull
    private LocalDate recruitmentStartDt;

    @NotNull
    private LocalDate recruitmentEndDt;

    @NotNull
    private LocalDate day;

    @NotNull
    private LocalTime time;

    @NotBlank
    private String category;

    @NotBlank
    private String address;

    @NotBlank
    private String detailedAddress;

    @NotNull
    private Double lat;

    @NotNull
    private Double lng;

    @NotBlank
    private String mainImg;

    @NotNull
    private ParticipantsType participantsType;

    @NotNull
    private Long fee;

    @NotNull
    private ParticipantSelectionMethod participantSelectionMethod;
}
