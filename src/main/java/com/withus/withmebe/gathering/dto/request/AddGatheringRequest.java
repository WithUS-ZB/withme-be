package com.withus.withmebe.gathering.dto.request;

import com.withus.withmebe.gathering.annotation.ValidDateRange;
import com.withus.withmebe.gathering.Type.GatheringType;
import com.withus.withmebe.gathering.Type.ParticipantSelectionMethod;
import com.withus.withmebe.gathering.Type.ParticipantsType;
import com.withus.withmebe.gathering.entity.Gathering;
import com.withus.withmebe.member.entity.Member;
import java.time.LocalDate;
import java.time.LocalTime;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ValidDateRange
public class AddGatheringRequest {

  @NotBlank
  private String title;

  @NotBlank
  private String content;

  @NotNull
  private GatheringType gatheringType;

  private Long maximumParticipant;

  @NotNull
  private LocalDate day;

  @NotNull
  private LocalTime time;

  @NotNull
  private LocalDate recruitmentStartDt;

  @NotNull
  private LocalDate recruitmentEndDt;

  @NotBlank
  private String category;

  @NotBlank
  private String address;

  @NotBlank
  private String detailedAddress;

  @NotNull
  private Double lat;

  @NotNull
  private Double lng;

  private MultipartFile mainImg;

  private MultipartFile subImg1;

  private MultipartFile subImg2;

  private MultipartFile subImg3;

  @NotNull
  private ParticipantsType participantsType;

  @NotNull
  private Long fee;

  @NotNull
  private ParticipantSelectionMethod participantSelectionMethod;

  public Gathering toEntity(Member newMember) {
    return Gathering.builder()
        .member(newMember)
        .title(this.title)
        .content(this.content)
        .gatheringType(this.gatheringType)
        .maximumParticipant(this.maximumParticipant)
        .day(this.day)
        .time(this.time)
        .recruitmentStartDt(this.recruitmentStartDt)
        .recruitmentEndDt(this.recruitmentEndDt)
        .category(this.category)
        .address(this.address)
        .detailedAddress(this.detailedAddress)
        .lat(this.lat)
        .lng(this.lng)
        .participantsType(this.participantsType)
        .fee(this.fee)
        .participantSelectionMethod(this.participantSelectionMethod)
        .build();
  }
}
