package com.withus.withmebe.gathering.controller;

import com.withus.withmebe.gathering.dto.response.LikedGatheringSimpleInfo;
import com.withus.withmebe.gathering.service.GatheringLikeService;
import com.withus.withmebe.security.anotation.CurrentMemberId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gathering/like")
public class GatheringLikeController {

  private final GatheringLikeService gatheringLikeService;

  @PutMapping
  public ResponseEntity<Boolean> updateLike(
      @CurrentMemberId long currentMemberId,
      @RequestParam(value = "gatheringid") long gatheringId) {
    return ResponseEntity.ok(gatheringLikeService.doLike(currentMemberId, gatheringId));
  }

  @GetMapping
  public ResponseEntity<Boolean> isLiked(
      @CurrentMemberId long currentMemberId,
      @RequestParam(value = "gatheringid") long gatheringId) {
    return ResponseEntity.ok(gatheringLikeService.isLiked(currentMemberId, gatheringId));
  }

  @GetMapping("/list")
  public ResponseEntity<Page<LikedGatheringSimpleInfo>> getLikedGatherings(
      @CurrentMemberId long currentMemberId,
      @PageableDefault Pageable pageable) {
    return ResponseEntity.ok(gatheringLikeService.readLikedGatherings(currentMemberId, pageable));
  }
}
