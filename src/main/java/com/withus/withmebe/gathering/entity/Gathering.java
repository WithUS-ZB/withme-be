package com.withus.withmebe.gathering.entity;

import com.withus.withmebe.common.entity.BaseEntity;
import com.withus.withmebe.gathering.Type.GatheringType;
import com.withus.withmebe.gathering.Type.ParticipantSelectionMethod;
import com.withus.withmebe.gathering.Type.ParticipantsType;
import com.withus.withmebe.gathering.Type.Status;
import com.withus.withmebe.gathering.dto.request.SetGatheringRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Where(clause = "deletedDttm is null")
@SQLDelete(sql = "UPDATE gathering SET deletedDttm = CURRENT_TIMESTAMP WHERE gathering_id = ?")
@EntityListeners(value = AuditingEntityListener.class)
public class Gathering extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private String category;

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
    private Status status = Status.PROGRESS;

    @Builder
    public Gathering(Long memberId, String title, String content, GatheringType gatheringType, Long maximumParticipant,
                     LocalDateTime startDttm, LocalDateTime endDttm, String category, LocalDateTime applicationDeadLine,
                     String address, String detailedAddress, String location, String mainImg,
                     ParticipantsType participantsType, Long fee,
                     ParticipantSelectionMethod participantSelectionMethod) {
        this.memberId = memberId;
        this.title = title;
        this.content = content;
        this.gatheringType = gatheringType;
        this.maximumParticipant = maximumParticipant;
        this.startDttm = startDttm;
        this.endDttm = endDttm;
        this.category = category;
        this.applicationDeadLine = applicationDeadLine;
        this.address = address;
        this.detailedAddress = detailedAddress;
        this.location = location;
        this.mainImg = mainImg;
        this.participantsType = participantsType;
        this.fee = fee;
        this.participantSelectionMethod = participantSelectionMethod;
    }

    public void updateGatheringFields(SetGatheringRequest setGatheringRequest) {
        title = setGatheringRequest.getTitle();
        content = setGatheringRequest.getContent();
        gatheringType = setGatheringRequest.getGatheringType();
        maximumParticipant = setGatheringRequest.getMaximumParticipant();
        startDttm = setGatheringRequest.getStartDttm();
        endDttm = setGatheringRequest.getEndDttm();
        applicationDeadLine = setGatheringRequest.getApplicationDeadLine();
        address = setGatheringRequest.getAddress();
        detailedAddress = setGatheringRequest.getDetailedAddress();
        location = setGatheringRequest.getLocation();
        mainImg = setGatheringRequest.getMainImg();
        participantsType = setGatheringRequest.getParticipantsType();
        category = setGatheringRequest.getCategory();
        fee = setGatheringRequest.getFee();
        participantSelectionMethod = setGatheringRequest.getParticipantSelectionMethod();
    }

}
