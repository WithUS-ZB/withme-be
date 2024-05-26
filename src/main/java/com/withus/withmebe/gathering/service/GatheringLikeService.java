package com.withus.withmebe.gathering.service;

import com.withus.withmebe.common.exception.CustomException;
import com.withus.withmebe.common.exception.ExceptionCode;
import com.withus.withmebe.gathering.dto.response.LikedGatheringSimpleInfo;
import com.withus.withmebe.gathering.entity.Gathering;
import com.withus.withmebe.gathering.entity.GatheringLike;
import com.withus.withmebe.gathering.event.DeleteGatheringEvent;
import com.withus.withmebe.gathering.repository.GatheringLikeRepository;
import com.withus.withmebe.gathering.repository.GatheringRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GatheringLikeService {

  private final GatheringLikeRepository gatheringLikeRepository;
  private final GatheringRepository gatheringRepository;

  @Transactional
  public boolean doLike(long requesterId, long gatheringId) {

    Optional<GatheringLike> optionalLike = gatheringLikeRepository.findByMemberIdAndGathering_Id(
        requesterId, gatheringId);

    GatheringLike gatheringLike = optionalLike.map(GatheringLike::updateIsLike)
        .orElseGet(() -> createLike(requesterId, gatheringId));
    updateLikeCount(gatheringLike.getGathering());
    return gatheringLike.getIsLiked();
  }

  @Transactional(readOnly = true)
  public boolean isLiked(long requesterId, long gatheringId) {
    return gatheringLikeRepository.existsGatheringLikeByMemberIdAndGathering_IdAndIsLikedIsTrue(
        requesterId, gatheringId);
  }

  public Page<LikedGatheringSimpleInfo> readLikedGatherings(long requesterId, Pageable pageable) {
    return gatheringLikeRepository.findByMemberIdAndIsLikedIsTrue(requesterId, pageable).map(GatheringLike::toLikedGatheringSimpleInfo);
  }


  private GatheringLike createLike(long memberId, long gatheringId) {

    return gatheringLikeRepository.save(GatheringLike.builder()
        .memberId(memberId)
        .gathering(readGathering((gatheringId)))
        .build());
  }

  private void updateLikeCount(Gathering gathering) {
    gathering.setLikeCount(
        gatheringLikeRepository.countGatheringLikesByGathering_IdAndIsLikedIsTrue(
            gathering.getId()));
  }

  private Gathering readGathering(long gatheringId) {
    return gatheringRepository.findById(gatheringId)
        .orElseThrow(() -> new CustomException(ExceptionCode.ENTITY_NOT_FOUND));
  }

  @EventListener
  @Async
  protected void deleteGatheringLike(DeleteGatheringEvent deleteGatheringEvent) {
    List<GatheringLike> gatheringLikes = gatheringLikeRepository.findAllByGathering_Id(deleteGatheringEvent.gatheringId());
    gatheringLikeRepository.deleteAll(gatheringLikes);
  }
}
