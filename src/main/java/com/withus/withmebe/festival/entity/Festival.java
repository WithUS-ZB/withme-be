package com.withus.withmebe.festival.entity;

import com.withus.withmebe.common.entity.BaseEntity;
import com.withus.withmebe.festival.dto.FestivalSimpleInfo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Festival extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "festival_id")
  private Long Id;

  private String title;

  @Column(length = 2000)
  private String img;

  @Column(length = 2000)
  private String link;

  private LocalDateTime startDttm;

  private LocalDateTime endDttm;

  public FestivalSimpleInfo toFestivalSimpleInfo() {
    return FestivalSimpleInfo.builder()
        .title(this.title)
        .img(this.img)
        .link(this.link)
        .build();
  }

  @Builder
  public Festival(String title, String img, String link, LocalDateTime startDttm,
      LocalDateTime endDttm) {
    this.title = title;
    this.img = img;
    this.link = link;
    this.startDttm = startDttm;
    this.endDttm = endDttm;
  }
}
