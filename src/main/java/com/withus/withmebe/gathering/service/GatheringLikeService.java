package com.withus.withmebe.gathering.service;

import com.withus.withmebe.gathering.entity.GatheringLike;
import com.withus.withmebe.gathering.repository.GatheringLikeRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GatheringLikeService {

  private final GatheringLikeRepository gatheringLikeRepository;

  @Transactional
  public boolean doLike(long requesterId, long gatheringId) {

    Optional<GatheringLike> optionalLike = gatheringLikeRepository.findByMemberIdAndGatheringId(requesterId,
        gatheringId);

    return optionalLike.map(this::updateLike)
        .orElseGet(() -> createLike(requesterId, gatheringId));
  }

  private boolean createLike(long memberId, long gatheringId) {

    GatheringLike gatheringLike = gatheringLikeRepository.save(GatheringLike.builder()
        .memberId(memberId)
        .gatheringId(gatheringId)
        .build());
    return gatheringLike.getIsLiked();
  }

  private boolean updateLike(GatheringLike gatheringLike) {

    gatheringLike.updateIsLike();
    return gatheringLike.getIsLiked();
  }
}
