package com.withus.withmebe.festival.entity;

import com.withus.withmebe.common.entity.BaseEntity;
import com.withus.withmebe.festival.dto.FestivalSimpleInfo;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Festival extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long Id;

  private String title;
  private String img;
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
}
