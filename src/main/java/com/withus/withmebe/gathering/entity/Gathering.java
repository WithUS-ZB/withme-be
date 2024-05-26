package com.withus.withmebe.gathering.entity;

import com.withus.withmebe.common.entity.BaseEntity;
import com.withus.withmebe.gathering.Type.GatheringType;
import com.withus.withmebe.gathering.Type.ParticipantSelectionMethod;
import com.withus.withmebe.gathering.Type.ParticipantsType;
import com.withus.withmebe.gathering.Type.Status;
import com.withus.withmebe.gathering.dto.request.SetGatheringRequest;
import com.withus.withmebe.gathering.dto.response.AddGatheringResponse;
import com.withus.withmebe.gathering.dto.response.DeleteGatheringResponse;
import com.withus.withmebe.gathering.dto.response.GetGatheringResponse;
import com.withus.withmebe.gathering.dto.response.SetGatheringResponse;
import com.withus.withmebe.gathering.service.GatheringService.Result;
import com.withus.withmebe.member.entity.Member;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Where(clause = "deleted_dttm is null")
@SQLDelete(sql = "UPDATE gathering SET deleted_dttm = CURRENT_TIMESTAMP, updated_dttm = CURRENT_TIMESTAMP WHERE gathering_id = ?")
public class Gathering extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "gathering_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @Column(nullable = false)
  private String title;

  @Column(columnDefinition = "TEXT")
  private String content;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private GatheringType gatheringType;

  private Long maximumParticipant;

  @Column(nullable = false)
  private LocalDate day;

  @Column(nullable = false)
  private LocalTime time;

  @Column(nullable = false)
  private LocalDate recruitmentStartDt;

  @Column(nullable = false)
  private LocalDate recruitmentEndDt;

  @Column(nullable = false)
  private String category;

  @Column(nullable = false)
  private String address;

  @Column(nullable = false)
  private String detailedAddress;

  @Column(nullable = false)
  private Double lat;

  @Column(nullable = false)
  private Double lng;

  private String mainImg;

  private String subImg1;

  private String subImg2;

  private String subImg3;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ParticipantsType participantsType;

  @Column(nullable = false)
  private Long fee;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ParticipantSelectionMethod participantSelectionMethod;

  private Long likeCount = 0L;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Status status = Status.PROGRESS;

  @Builder
  public Gathering(Member member,String title, String content, GatheringType gatheringType,
      Long maximumParticipant,
      LocalDate day, LocalTime time,
      LocalDate recruitmentStartDt, LocalDate recruitmentEndDt, String category,
      String address, String detailedAddress, Double lat, Double lng, String mainImg,
      String subImg1, String subImg2, String subImg3,
      ParticipantsType participantsType, Long fee,
      ParticipantSelectionMethod participantSelectionMethod) {
    this.member = member;
    this.title = title;
    this.content = content;
    this.gatheringType = gatheringType;
    this.maximumParticipant = maximumParticipant;
    this.recruitmentStartDt = recruitmentStartDt;
    this.recruitmentEndDt = recruitmentEndDt;
    this.day = day;
    this.time = time;
    this.category = category;
    this.address = address;
    this.detailedAddress = detailedAddress;
    this.lat = lat;
    this.lng = lng;
    this.mainImg = mainImg;
    this.subImg1 = subImg1;
    this.subImg2 = subImg2;
    this.subImg3 = subImg3;
    this.participantsType = participantsType;
    this.fee = fee;
    this.participantSelectionMethod = participantSelectionMethod;
  }

  public AddGatheringResponse toAddGatheringResponse() {
    return AddGatheringResponse.builder()
        .memberId(this.member.getId())
        .gatheringId(this.id)
        .title(this.title)
        .content(this.content)
        .gatheringType(this.gatheringType)
        .maximumParticipant(this.maximumParticipant)
        .recruitmentStartDt(this.recruitmentStartDt)
        .recruitmentEndDt(this.recruitmentEndDt)
        .day(this.day)
        .time(this.time)
        .category(this.category)
        .address(this.address)
        .detailedAddress(this.detailedAddress)
        .lat(this.lat)
        .lng(this.lng)
        .mainImg(this.mainImg)
        .subImg1(this.subImg1)
        .subImg2(this.subImg2)
        .subImg3(this.subImg3)
        .participantsType(this.participantsType)
        .fee(this.fee)
        .participantSelectionMethod(this.participantSelectionMethod)
        .build();
  }

  public SetGatheringResponse toSetGatheringResponse() {
    return SetGatheringResponse.builder()
        .memberId(this.member.getId())
        .gatheringId(this.id)
        .title(this.title)
        .content(this.content)
        .gatheringType(this.gatheringType)
        .maximumParticipant(this.maximumParticipant)
        .recruitmentStartDt(this.recruitmentStartDt)
        .recruitmentEndDt(this.recruitmentEndDt)
        .day(this.day)
        .time(this.time)
        .category(this.category)
        .address(this.address)
        .detailedAddress(this.detailedAddress)
        .lat(this.lat)
        .lng(this.lng)
        .mainImg(this.mainImg)
        .subImg1(this.subImg1)
        .subImg2(this.subImg2)
        .subImg3(this.subImg3)
        .participantsType(this.participantsType)
        .fee(this.fee)
        .participantSelectionMethod(this.participantSelectionMethod)
        .likeCount(this.likeCount)
        .build();
  }

  public GetGatheringResponse toGetGatheringResponse() {
    return GetGatheringResponse.builder()
        .memberId(this.member.getId())
        .gatheringId(this.id)
        .status(this.status)
        .profileImg(this.member.getProfileImg())
        .nickName(this.member.getNickName())
        .title(this.title)
        .content(this.content)
        .gatheringType(this.gatheringType)
        .maximumParticipant(this.maximumParticipant)
        .recruitmentStartDt(this.recruitmentStartDt)
        .recruitmentEndDt(this.recruitmentEndDt)
        .day(this.day)
        .time(this.time)
        .category(this.category)
        .address(this.address)
        .detailedAddress(this.detailedAddress)
        .lat(this.lat)
        .lng(this.lng)
        .mainImg(this.mainImg)
        .subImg1(this.subImg1)
        .subImg2(this.subImg2)
        .subImg3(this.subImg3)
        .participantsType(this.participantsType)
        .fee(this.fee)
        .participantSelectionMethod(this.participantSelectionMethod)
        .likeCount(this.likeCount)
        .createdDttm(this.getCreatedDttm())
        .build();
  }

  public DeleteGatheringResponse toDeleteGatheringResponse() {
    return DeleteGatheringResponse.builder()
        .memberId(this.member.getId())
        .gatheringId(this.id)
        .title(this.title)
        .content(this.content)
        .gatheringType(this.gatheringType)
        .maximumParticipant(this.maximumParticipant)
        .recruitmentStartDt(this.recruitmentStartDt)
        .recruitmentEndDt(this.recruitmentEndDt)
        .day(this.day)
        .time(this.time)
        .category(this.category)
        .address(this.address)
        .detailedAddress(this.detailedAddress)
        .lat(this.lat)
        .lng(this.lng)
        .mainImg(this.mainImg)
        .participantsType(this.participantsType)
        .fee(this.fee)
        .participantSelectionMethod(this.participantSelectionMethod)
        .likeCount(this.likeCount)
        .build();
  }

  public void updateGatheringFields(SetGatheringRequest setGatheringRequest) {
    title = setGatheringRequest.getTitle();
    content = setGatheringRequest.getContent();
    gatheringType = setGatheringRequest.getGatheringType();
    maximumParticipant = setGatheringRequest.getMaximumParticipant();
    recruitmentStartDt = setGatheringRequest.getRecruitmentStartDt();
    recruitmentEndDt = setGatheringRequest.getRecruitmentEndDt();
    day = setGatheringRequest.getDay();
    time = setGatheringRequest.getTime();
    address = setGatheringRequest.getAddress();
    detailedAddress = setGatheringRequest.getDetailedAddress();
    lat = setGatheringRequest.getLat();
    lng = setGatheringRequest.getLng();
    participantsType = setGatheringRequest.getParticipantsType();
    category = setGatheringRequest.getCategory();
    fee = setGatheringRequest.getFee();
    participantSelectionMethod = setGatheringRequest.getParticipantSelectionMethod();
  }

  public void initiateGatheringImages(Result s3UpdateUrl) {
    mainImg = s3UpdateUrl.mainImgUrl();
    subImg1 = s3UpdateUrl.subImgUrl1();
    subImg2 = s3UpdateUrl.subImgUrl2();
    subImg3 = s3UpdateUrl.subImgUrl3();
  }

  public boolean isHost(Long memberId){
    return this.member.getId().equals(memberId);
  }

  public LocalDateTime getGatheringDateTime(){
    return LocalDateTime.of(this.day, this.time);
  }
  
  public void updateGatheringImages(Result s3UpdateUrl) {
    mainImg = (s3UpdateUrl.mainImgUrl().isEmpty())? mainImg:s3UpdateUrl.mainImgUrl();
    subImg1 = (s3UpdateUrl.subImgUrl1().isEmpty())? subImg1:s3UpdateUrl.subImgUrl1();
    subImg2 = (s3UpdateUrl.subImgUrl2().isEmpty())? subImg2:s3UpdateUrl.subImgUrl2();
    subImg3 = (s3UpdateUrl.subImgUrl3().isEmpty())? subImg3:s3UpdateUrl.subImgUrl3();
  }

}