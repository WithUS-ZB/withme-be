package com.withus.withmebe.participation.controller;

import com.withus.withmebe.participation.dto.ParticipationResponse;
import com.withus.withmebe.participation.service.ParticipationService;
import com.withus.withmebe.security.anotation.CurrentMemberId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    return ResponseEntity.ok(participationService.createParticipation(currentMemberId, gatheringId));
  }
}
