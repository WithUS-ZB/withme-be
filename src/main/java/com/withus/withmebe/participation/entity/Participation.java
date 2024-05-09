package com.withus.withmebe.participation.entity;

import com.withus.withmebe.common.entity.BaseEntity;
import com.withus.withmebe.gathering.entity.Gathering;
import com.withus.withmebe.member.entity.Member;
import com.withus.withmebe.participation.dto.ParticipationResponse;
import com.withus.withmebe.participation.dto.ParticipationSimpleInfo;
import com.withus.withmebe.participation.type.Status;
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

@Entity
@Getter
@NoArgsConstructor
public class Participation extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(nullable = false)
  private Gathering gathering;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(nullable = false)
  private Member member;

  @Enumerated(EnumType.STRING)
  private Status status;

  @Builder
  public Participation(Gathering gathering, Member member, Status status) {
    this.gathering = gathering;
    this.member = member;
    this.status = status;
  }

  public ParticipationResponse toResponse() {
    return ParticipationResponse.builder()
        .id(this.id)
        .nickName(this.member.getNickName())
        .title(this.gathering.getTitle())
        .status(this.status)
        .createdDttm(this.getCreatedDttm())
        .updatedDttm(this.getUpdatedDttm())
        .build();
  }

  public ParticipationSimpleInfo toSimpleInfo() {
    return ParticipationSimpleInfo.builder()
        .id(this.id)
        .nickName(this.member.getNickName())
        .status(this.status)
        .updatedDttm(this.getUpdatedDttm())
        .build();
  }
}
