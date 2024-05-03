package com.withus.withmebe.gathering.entity;

import static jakarta.persistence.GenerationType.IDENTITY;

import com.withus.withmebe.common.entity.BaseEntity;
import com.withus.withmebe.gathering.Type.GatheringType;
import com.withus.withmebe.gathering.Type.ParticipantSelectionMethod;
import com.withus.withmebe.gathering.Type.ParticipantsType;
import com.withus.withmebe.gathering.Type.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Gathering extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "gathering_id")
    private Long id;

    @Column(nullable = false)
    private Long memberId;

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

    private Long likeCount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private final Status status = Status.PROGRESS;

    @Builder
    public Gathering(Long memberId, String title, String content, GatheringType gatheringType, Long maximumParticipant,
                     LocalDateTime startDttm, LocalDateTime endDttm, LocalDateTime applicationDeadLine, String address,
                     String detailedAddress, String location, String mainImg, ParticipantsType participantsType,
                     Long fee,
                     ParticipantSelectionMethod participantSelectionMethod) {
        this.memberId = memberId;
        this.title = title;
        this.content = content;
        this.gatheringType = gatheringType;
        this.maximumParticipant = maximumParticipant;
        this.startDttm = startDttm;
        this.endDttm = endDttm;
        this.applicationDeadLine = applicationDeadLine;
        this.address = address;
        this.detailedAddress = detailedAddress;
        this.location = location;
        this.mainImg = mainImg;
        this.participantsType = participantsType;
        this.fee = fee;
        this.participantSelectionMethod = participantSelectionMethod;
    }
}
