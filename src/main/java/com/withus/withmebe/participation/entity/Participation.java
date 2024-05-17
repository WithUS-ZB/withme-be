package com.withus.withmebe.participation.entity;

import com.withus.withmebe.common.entity.BaseEntity;
import com.withus.withmebe.gathering.entity.Gathering;
import com.withus.withmebe.member.entity.Member;
import com.withus.withmebe.participation.dto.MyParticipationSimpleInfo;
import com.withus.withmebe.participation.dto.ParticipationResponse;
import com.withus.withmebe.participation.dto.GatheringParticipationSimpleInfo;
import com.withus.withmebe.participation.type.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
public class Participation extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "participation_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(nullable = false, name = "gathering_id")
  private Gathering gathering;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(nullable = false, name = "member_id")
  private Member participant;

  @Setter
  @Enumerated(EnumType.STRING)
  private Status status;

  @Builder
  public Participation(Gathering gathering, Member member, Status status) {
    this.gathering = gathering;
    this.participant = member;
    this.status = status;
  }

  public ParticipationResponse toResponse() {
    return ParticipationResponse.builder()
        .id(this.id)
        .nickName(this.participant.getNickName())
        .title(this.gathering.getTitle())
        .status(this.status)
        .createdDttm(this.getCreatedDttm())
        .updatedDttm(this.getUpdatedDttm())
        .build();
  }

  public GatheringParticipationSimpleInfo toGatheringParticipationSimpleInfo() {
    return GatheringParticipationSimpleInfo.builder()
        .id(this.id)
        .nickName(this.participant.getNickName())
        .status(this.status)
        .updatedDttm(this.getUpdatedDttm())
        .build();
  }

  public MyParticipationSimpleInfo toMyParticipationSimpleInfo() {
    return MyParticipationSimpleInfo.builder()
        .id(this.id)
        .title(this.gathering.getTitle())
        .status(this.status)
        .updatedDttm(this.getUpdatedDttm())
        .build();
  }

  public boolean isParticipant(long memberId) {
    return this.participant.getId() == memberId;
  }

  public boolean statusEquals(Status status) {
    return this.status == status;
  }
}
