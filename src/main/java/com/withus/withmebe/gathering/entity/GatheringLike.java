package com.withus.withmebe.gathering.entity;

import com.withus.withmebe.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class GatheringLike extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "like_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "gathering_id", nullable = false)
  private Gathering gathering;

  @Column(nullable = false)
  private Long memberId;
  @Column(nullable = false)
  private Boolean isLiked = true;

  @Builder
  public GatheringLike(Gathering gathering, long memberId) {
    this.gathering = gathering;
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
