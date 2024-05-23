package com.withus.withmebe.gathering.entity;

import com.withus.withmebe.common.entity.BaseEntity;
import com.withus.withmebe.gathering.dto.response.LikedGatheringSimpleInfo;
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
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@NoArgsConstructor
@Where(clause = "deleted_dttm is null")
@SQLDelete(sql = "UPDATE gathering_like SET deleted_dttm = CURRENT_TIMESTAMP, updated_dttm = CURRENT_TIMESTAMP WHERE like_id = ?")
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

  public LikedGatheringSimpleInfo toLikedGatheringSimpleInfo() {
    return LikedGatheringSimpleInfo.builder()
        .id(this.id)
        .gatheringId(this.gathering.getId())
        .title(this.gathering.getTitle())
        .mainImg(this.gathering.getMainImg())
        .day(this.gathering.getDay())
        .time(this.gathering.getTime())
        .gatheringType(this.gathering.getGatheringType())
        .likeCount(this.gathering.getLikeCount())
        .updatedDttm(this.getUpdatedDttm())
        .build();
  }
}
