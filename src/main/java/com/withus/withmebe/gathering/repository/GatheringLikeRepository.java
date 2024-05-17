package com.withus.withmebe.gathering.repository;

import com.withus.withmebe.gathering.entity.GatheringLike;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GatheringLikeRepository extends JpaRepository<GatheringLike, Long> {

  Optional<GatheringLike> findByMemberIdAndGatheringId(Long memberId, Long gatheringId);

  Long countByGatheringIdAndIsLikedIsTrue(Long gatheringId);
}
