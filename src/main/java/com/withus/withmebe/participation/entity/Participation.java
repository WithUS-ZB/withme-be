package com.withus.withmebe.participation.entity;

import com.withus.withmebe.chat.dto.response.ParticipationInfoOfChatroom;
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
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@NoArgsConstructor
@Where(clause = "deleted_dttm is null")
@SQLDelete(sql = "UPDATE participation SET deleted_dttm = CURRENT_TIMESTAMP, updated_dttm = CURRENT_TIMESTAMP WHERE participation_id = ?")
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
  public Participation(Gathering gathering, Member participant, Status status) {
    this.gathering = gathering;
    this.participant = participant;
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
        .birthDate(this.participant.getBirthDate())
        .gender(this.participant.getGender())
        .profileImg(this.participant.getProfileImg())
        .status(this.status)
        .createdDttm(this.getCreatedDttm())
        .updatedDttm(this.getUpdatedDttm())
        .build();
  }

  public MyParticipationSimpleInfo toMyParticipationSimpleInfo() {
    return MyParticipationSimpleInfo.builder()
        .id(this.id)
        .gatheringId(this.gathering.getId())
        .title(this.gathering.getTitle())
        .mainImg(this.gathering.getMainImg())
        .day(this.gathering.getDay())
        .time(this.gathering.getTime())
        .gatheringType(this.gathering.getGatheringType())
        .gatheringStatus(this.gathering.getStatus())
        .status(this.status)
        .updatedDttm(this.getUpdatedDttm())
        .build();
  }

  public ParticipationInfoOfChatroom toParticipationInfoOfChatroom(Long chatroomId, String chatroomTitle){
    return ParticipationInfoOfChatroom.builder()
        .participationId(this.id)
        .chatroomId(chatroomId)
        .memberId(this.participant.getId())
        .nickname(this.participant.getNickName())
        .chatroomTitle(chatroomTitle)
        .participationStatus(this.status)
        .build();
  }

  public boolean isParticipant(long memberId) {
    return this.participant.getId() == memberId;
  }

  public boolean statusEquals(Status status) {
    return this.status == status;
  }

  public boolean isValidParticipationStatus() {
    return !(this.status.equals(Status.CANCELED) || this.status.equals(Status.REJECTED));
  }
}
