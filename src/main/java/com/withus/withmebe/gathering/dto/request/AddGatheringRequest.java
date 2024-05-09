package com.withus.withmebe.gathering.dto.request;

import com.withus.withmebe.gathering.Type.GatheringType;
import com.withus.withmebe.gathering.Type.ParticipantSelectionMethod;
import com.withus.withmebe.gathering.Type.ParticipantsType;
import com.withus.withmebe.gathering.entity.Gathering;
import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AddGatheringRequest {
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

    public Gathering toEntity(long memberId) {
        return Gathering.builder()
                .memberId(memberId)
                .title(this.title)
                .content(this.content)
                .gatheringType(this.gatheringType)
                .maximumParticipant(this.maximumParticipant)
                .startDttm(this.startDttm)
                .endDttm(this.endDttm)
                .category(this.category)
                .applicationDeadLine(this.applicationDeadLine)
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
