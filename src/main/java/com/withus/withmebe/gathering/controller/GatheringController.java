package com.withus.withmebe.gathering.controller;

import com.withus.withmebe.gathering.dto.request.AddGatheringRequest;
import com.withus.withmebe.gathering.dto.request.SetGatheringRequest;
import com.withus.withmebe.gathering.dto.response.AddGatheringResponse;
import com.withus.withmebe.gathering.dto.response.DeleteGatheringResponse;
import com.withus.withmebe.gathering.dto.response.GetGatheringResponse;
import com.withus.withmebe.gathering.dto.response.SetGatheringResponse;
import com.withus.withmebe.gathering.service.GatheringService;
import com.withus.withmebe.security.anotation.CurrentMemberId;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gathering")
public class GatheringController {

  private final GatheringService gatheringService;

  @PostMapping()
  public ResponseEntity<AddGatheringResponse> addGathering(@CurrentMemberId long currentMemberId,
      @Valid @RequestPart(required = false) AddGatheringRequest addGatheringRequest,
      @RequestPart(value = "mainImg", required = false) MultipartFile mainImg,
      @RequestPart(value = "subImg1", required = false) MultipartFile subImg1,
      @RequestPart(value = "subImg2", required = false) MultipartFile subImg2,
      @RequestPart(value = "subImg3", required = false) MultipartFile subImg3) {
    return ResponseEntity.ok(
        gatheringService.createGathering(currentMemberId, addGatheringRequest, mainImg, subImg1,
            subImg2, subImg3));
  }

  @GetMapping("/list")
  public ResponseEntity<List<GetGatheringResponse>> getGatheringList() {
    return ResponseEntity.ok(gatheringService.readGatheringList());
  }

  @GetMapping("/myList")
  public ResponseEntity<List<GetGatheringResponse>> getGatheringMyList(
      @CurrentMemberId long currentMemberId) {
    return ResponseEntity.ok(gatheringService.readGatheringMyList(currentMemberId));
  }

  @GetMapping("/{gatheringId}")
  public ResponseEntity<GetGatheringResponse> getGathering(@PathVariable long gatheringId) {
    return ResponseEntity.ok(gatheringService.readGathering(gatheringId));
  }

  @PutMapping("/{gatheringId}")
  public ResponseEntity<SetGatheringResponse> setGathering(@CurrentMemberId long currentMemberId,
      @PathVariable long gatheringId,
      @Valid @RequestBody SetGatheringRequest setGatheringRequest) {
    return ResponseEntity.ok(
        gatheringService.updateGathering(currentMemberId, gatheringId, setGatheringRequest));
  }

  @DeleteMapping("/{gatheringId}")
  public ResponseEntity<DeleteGatheringResponse> removeGathering(
      @CurrentMemberId long currentMemberId,
      @PathVariable long gatheringId) {
    return ResponseEntity.ok(gatheringService.deleteGathering(currentMemberId, gatheringId));
  }
}
