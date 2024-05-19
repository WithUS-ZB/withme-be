package com.withus.withmebe.gathering.service;

import com.withus.withmebe.common.exception.CustomException;
import com.withus.withmebe.common.exception.ExceptionCode;
import com.withus.withmebe.gathering.entity.Gathering;
import com.withus.withmebe.gathering.entity.GatheringLike;
import com.withus.withmebe.gathering.repository.GatheringLikeRepository;
import com.withus.withmebe.gathering.repository.GatheringRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GatheringLikeService {

  private final GatheringLikeRepository gatheringLikeRepository;
  private final GatheringRepository gatheringRepository;

  @Transactional
  public boolean doLike(long requesterId, long gatheringId) {

    Gathering gathering = readGathering(gatheringId);

    Optional<GatheringLike> optionalLike = gatheringLikeRepository.findByMemberIdAndGathering(
        requesterId, gathering);

    boolean result = optionalLike.map(this::updateLike)
        .orElseGet(() -> createLike(requesterId, gathering));
    updateLikeCount(gathering);
    return result;
  }

  private boolean createLike(long memberId, Gathering gathering) {

    GatheringLike gatheringLike = gatheringLikeRepository.save(GatheringLike.builder()
        .memberId(memberId)
        .gathering(gathering)
        .build());
    return gatheringLike.getIsLiked();
  }

  private boolean updateLike(GatheringLike gatheringLike) {

    gatheringLike.updateIsLike();
    return gatheringLike.getIsLiked();
  }

  private void updateLikeCount(Gathering gathering) {
    gathering.setLikeCount(
        gatheringLikeRepository.countGatheringLikeByGatheringAndAndIsLikedIsTrue(gathering));
  }

  private Gathering readGathering(long gatheringId) {
    return gatheringRepository.findById(gatheringId)
        .orElseThrow(() -> new CustomException(ExceptionCode.ENTITY_NOT_FOUND));
  }
}
