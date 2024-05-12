package com.withus.withmebe.participation.controller;

import static com.withus.withmebe.participation.type.Status.APPROVED;
import static com.withus.withmebe.participation.type.Status.REJECTED;

import com.withus.withmebe.participation.dto.MyParticipationSimpleInfo;
import com.withus.withmebe.participation.dto.ParticipationResponse;
import com.withus.withmebe.participation.dto.GatheringParticipationSimpleInfo;
import com.withus.withmebe.participation.service.ParticipationService;
import com.withus.withmebe.security.anotation.CurrentMemberId;
import com.withus.withmebe.security.anotation.CurrentUserDetailsDomain;
import com.withus.withmebe.security.domain.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
      @CurrentUserDetailsDomain CustomUserDetails currentUserDetails,
      @RequestParam(value = "gatheringid") long gatheringId) {
    return ResponseEntity.ok(
        participationService.createParticipation(currentUserDetails, gatheringId));
  }

  @GetMapping("/count")
  public ResponseEntity<Long> getApprovedParticipationCount(
      @RequestParam(value = "gatheringid") long gatheringId) {
    return ResponseEntity.ok(participationService.readApprovedParticipationCount(gatheringId));
  }

  @GetMapping("/list")
  public ResponseEntity<Page<GatheringParticipationSimpleInfo>> getParticipations(
      @CurrentMemberId long currentMemberId,
      @RequestParam(value = "gatheringid") long gatheringId,
      @PageableDefault Pageable pageble) {
    return ResponseEntity.ok(
        participationService.readParticipations(currentMemberId, gatheringId, pageble));
  }

  @PutMapping("/cancel/{participationId}")
  public ResponseEntity<ParticipationResponse> cancelParticipation(
      @CurrentMemberId long currentMemberId, @PathVariable long participationId) {
    return ResponseEntity.ok(
        participationService.cancelParticipation(currentMemberId, participationId));
  }

  @PutMapping("/approve/{participationId}")
  public ResponseEntity<ParticipationResponse> approveParticipation(
      @CurrentMemberId long currentMemberId, @PathVariable long participationId) {
    return ResponseEntity.ok(
        participationService.updateParticipationStatus(currentMemberId, participationId,
            APPROVED));
  }

  @PutMapping("/reject/{participationId}")
  public ResponseEntity<ParticipationResponse> rejectParticipation(
      @CurrentMemberId long currentMemberId, @PathVariable long participationId) {
    return ResponseEntity.ok(
        participationService.updateParticipationStatus(currentMemberId, participationId, REJECTED));
  }

  @GetMapping("/{participationId}")
  public ResponseEntity<ParticipationResponse> getMyParticipation(
      @CurrentMemberId long currentMemberId, @PathVariable long participationId) {
    return ResponseEntity.ok(
        participationService.readMyParticipation(currentMemberId, participationId));
  }

  @GetMapping("/mylist")
  public ResponseEntity<Page<MyParticipationSimpleInfo>> getMyParticipations(
      @CurrentMemberId long currentMemberId, @PageableDefault Pageable pageble) {
    return ResponseEntity.ok(
        participationService.readMyParticipations(currentMemberId, pageble));
  }
}
