package com.withus.withmebe.gathering.dto.request;

import com.withus.withmebe.gathering.Type.GatheringType;
import com.withus.withmebe.gathering.Type.ParticipantSelectionMethod;
import com.withus.withmebe.gathering.Type.ParticipantsType;
import com.withus.withmebe.gathering.entity.Gathering;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AddGatheringRequest {
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

    public Gathering toEntity(long memberId) {
        return Gathering.builder()
                .memberId(memberId)
                .title(this.title)
                .content(this.content)
                .gatheringType(this.gatheringType)
                .maximumParticipant(this.maximumParticipant)
                .day(this.day)
                .time(this.time)
                .recruitmentStartDt(this.recruitmentStartDt)
                .recruitmentEndDt(this.recruitmentEndDt)
                .category(this.category)
                .address(this.address)
                .detailedAddress(this.detailedAddress)
                .location(this.location)
                .mainImg(this.mainImg)
                .participantsType(this.participantsType)
                .fee(this.fee)
                .participantSelectionMethod(this.participantSelectionMethod)
                .build();
    }
}
