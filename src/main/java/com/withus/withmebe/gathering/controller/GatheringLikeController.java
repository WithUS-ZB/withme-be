package com.withus.withmebe.gathering.controller;

import com.withus.withmebe.gathering.service.GatheringLikeService;
import com.withus.withmebe.security.anotation.CurrentMemberId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
  public ResponseEntity<Boolean> doLike(
      @CurrentMemberId long currentMemberId,
      @RequestParam(value = "gatheringid") long gatheringId) {
    return ResponseEntity.ok(gatheringLikeService.doLike(currentMemberId, gatheringId));
  }
}
