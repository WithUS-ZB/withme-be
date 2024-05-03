package com.withus.withmebe.gathering.dto.request;

import com.withus.withmebe.gathering.Type.GatheringType;
import com.withus.withmebe.gathering.Type.ParticipantSelectionMethod;
import com.withus.withmebe.gathering.Type.ParticipantsType;
import com.withus.withmebe.gathering.entity.Gathering;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor (force = true)
@AllArgsConstructor
@ToString
public class AddGatheringRequest {
    private String memberId;

    @Column(nullable = false)
    private String title;

    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GatheringType gatheringType;

    private Long maximumParticipant;

    @Column(nullable = false)
    private LocalDateTime startDttm;

    @Column(nullable = false)
    private LocalDateTime endDttm;

    @Column(nullable = false)
    private LocalDateTime applicationDeadLine;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String detailedAddress;

    private String location;

    @Column(nullable = false)
    private String mainImg;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ParticipantsType participantsType;

    @Column(nullable = false)
    private Long fee;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ParticipantSelectionMethod participantSelectionMethod;

    @Builder
    public Gathering toEntity(long memberId) {
        return Gathering.builder()
                .memberId(memberId)
                .title(this.title)
                .content(this.content)
                .gatheringType(this.gatheringType)
                .maximumParticipant(this.maximumParticipant)
                .startDttm(this.startDttm)
                .endDttm(this.endDttm)
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
