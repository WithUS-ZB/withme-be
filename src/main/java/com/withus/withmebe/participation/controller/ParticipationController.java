package com.withus.withmebe.participation.controller;

import com.withus.withmebe.participation.dto.ParticipationResponse;
import com.withus.withmebe.participation.dto.ParticipationSimpleInfo;
import com.withus.withmebe.participation.service.ParticipationService;
import com.withus.withmebe.security.anotation.CurrentMemberId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/participation")
public class ParticipationController {

  private final ParticipationService participationService;

  @PostMapping
  public ResponseEntity<ParticipationResponse> addParticipation(
      @CurrentMemberId long currentMemberId,
      @RequestParam(value = "gatheringid") long gatheringId) {
    return ResponseEntity.ok(
        participationService.createParticipation(currentMemberId, gatheringId));
  }

  @GetMapping("/count")
  public ResponseEntity<Long> getApprovedParticipationCount(
      @RequestParam(value = "gatheringid") long gatheringId) {
    return ResponseEntity.ok(participationService.readApprovedParticipationCount(gatheringId));
  }

  @GetMapping("/list")
  public ResponseEntity<Page<ParticipationSimpleInfo>> getParticipations(
      @CurrentMemberId long currentMemberId,
      @RequestParam(value = "gatheringid") long gatheringId,
      @PageableDefault Pageable pageble) {
    return ResponseEntity.ok(participationService.readParticipations(currentMemberId, gatheringId, pageble));
  }
}
