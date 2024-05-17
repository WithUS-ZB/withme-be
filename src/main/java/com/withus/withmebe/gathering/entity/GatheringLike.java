package com.withus.withmebe.gathering.entity;

import com.withus.withmebe.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class GatheringLike extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "like_id")
  private Long id;

  @Column(nullable = false)
  private Long gatheringId;

  @Column(nullable = false)
  private Long memberId;

  @Getter
  @Column(nullable = false)
  private Boolean isLiked = true;

  @Builder
  public GatheringLike(long gatheringId, long memberId) {
    this.gatheringId = gatheringId;
    this.memberId = memberId;
  }

  public GatheringLike updateIsLike() {
    this.isLiked = !this.isLiked;
    return this;
  }

  public boolean isMember(long memberId) {
    return this.memberId == memberId;
  }
}
