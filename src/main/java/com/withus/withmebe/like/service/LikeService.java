package com.withus.withmebe.like.service;

import com.withus.withmebe.common.exception.CustomException;
import com.withus.withmebe.common.exception.ExceptionCode;
import com.withus.withmebe.like.entity.Like;
import com.withus.withmebe.like.repository.LikeRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

  private final LikeRepository likeRepository;

  @Transactional
  public boolean doLike(long requester, long gatheringId) {

    Optional<Like> optionalLike = likeRepository.findByMemberIdAndGatheringId(requester,
        gatheringId);

    return optionalLike.map(like -> updateLike(requester, like))
        .orElseGet(() -> createLike(requester, gatheringId));
  }

  private boolean createLike(long memberId, long gatheringId) {

    Like like = likeRepository.save(Like.builder()
        .memberId(memberId)
        .gatheringId(gatheringId)
        .build());
    return like.getIsLiked();
  }

  private boolean updateLike(long memberId, Like like) {

    validateRequester(memberId, like);
    like.updateIsLike();
    return like.getIsLiked();
  }

  private void validateRequester(long memberId, Like like) {

    if (!like.isMember(memberId)) {
      throw new CustomException(ExceptionCode.AUTHORIZATION_ISSUE);
    }
  }
}
