package com.withus.withmebe.gathering.dto.request;

import com.withus.withmebe.gathering.Type.GatheringType;
import com.withus.withmebe.gathering.Type.ParticipantSelectionMethod;
import com.withus.withmebe.gathering.Type.ParticipantsType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor (force = true)
@AllArgsConstructor
public class AddGatheringRequest {

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


}
