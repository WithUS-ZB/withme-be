package com.withus.withmebe.gathering.service;

import com.withus.withmebe.common.exception.CustomException;
import com.withus.withmebe.common.exception.ExceptionCode;
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
  public boolean doLike(long requester, long gatheringId) {

    Optional<GatheringLike> optionalLike = gatheringLikeRepository.findByMemberIdAndGatheringId(requester,
        gatheringId);

    return optionalLike.map(gatheringLike -> updateLike(requester, gatheringLike))
        .orElseGet(() -> createLike(requester, gatheringId));
  }

  private boolean createLike(long memberId, long gatheringId) {

    GatheringLike gatheringLike = gatheringLikeRepository.save(GatheringLike.builder()
        .memberId(memberId)
        .gatheringId(gatheringId)
        .build());
    return gatheringLike.getIsLiked();
  }

  private boolean updateLike(long memberId, GatheringLike gatheringLike) {

    validateRequester(memberId, gatheringLike);
    gatheringLike.updateIsLike();
    return gatheringLike.getIsLiked();
  }

  private void validateRequester(long memberId, GatheringLike gatheringLike) {

    if (!gatheringLike.isMember(memberId)) {
      throw new CustomException(ExceptionCode.AUTHORIZATION_ISSUE);
    }
  }
}
