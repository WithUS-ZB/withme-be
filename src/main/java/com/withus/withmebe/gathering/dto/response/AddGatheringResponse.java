package com.withus.withmebe.gathering.dto.response;

import com.withus.withmebe.gathering.Type.GatheringType;
import com.withus.withmebe.gathering.Type.ParticipantSelectionMethod;
import com.withus.withmebe.gathering.Type.ParticipantsType;
import java.time.LocalDate;
import java.time.LocalTime;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AddGatheringResponse {
    @NotNull
    private Long memberId;

    @NotNull
    private Long gatheringId;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotNull
    private GatheringType gatheringType;

    private Long maximumParticipant;

    @NotNull
    private LocalDate day;

    @NotNull
    private LocalTime time;

    @NotNull
    private LocalDate recruitmentStartDt;

    @NotNull
    private LocalDate recruitmentEndDt;

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

    private String mainImg;

    private String subImg1;

    private String subImg2;

    private String subImg3;

    @NotNull
    private ParticipantsType participantsType;

    @NotNull
    private Long fee;

    @NotNull
    private ParticipantSelectionMethod participantSelectionMethod;
}
