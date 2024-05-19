package com.withus.withmebe.gathering.repository;

import com.withus.withmebe.gathering.entity.GatheringLike;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GatheringLikeRepository extends JpaRepository<GatheringLike, Long> {

  @EntityGraph(attributePaths = "gathering")
  Optional<GatheringLike> findByMemberIdAndGathering_Id(Long memberId, Long gatheringId);

  Long countGatheringLikesByGathering_IdAndIsLikedIsTrue(Long gatheringId);
}
