package com.withus.withmebe.like.repository;

import com.withus.withmebe.like.entity.Like;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

  Optional<Like> findByMemberIdAndGatheringId(Long memberId, Long gatheringId);
}
