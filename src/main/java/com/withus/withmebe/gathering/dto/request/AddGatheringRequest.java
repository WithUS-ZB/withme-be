package com.withus.withmebe.gathering.dto.request;

import com.withus.withmebe.gathering.Type.GatheringType;
import com.withus.withmebe.gathering.Type.ParticipantSelectionMethod;
import com.withus.withmebe.gathering.Type.ParticipantsType;
import com.withus.withmebe.gathering.entity.Gathering;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(force = true)
@AllArgsConstructor
@ToString
public class AddGatheringRequest {
    private String memberId;

    @NotNull
    private String title;

    private String content;

    @NotNull
    private GatheringType gatheringType;

    private Long maximumParticipant;

    @NotNull
    private LocalDateTime startDttm;

    @NotNull
    private LocalDateTime endDttm;

    @NotNull
    private String category;

    @NotNull
    private LocalDateTime applicationDeadLine;

    @NotNull
    private String address;

    @NotNull
    private String detailedAddress;

    private String location;

    @NotNull
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
