package com.withus.withmebe.gathering.controller;

import com.withus.withmebe.gathering.dto.request.AddGatheringRequest;
import com.withus.withmebe.gathering.dto.request.SetGatheringRequest;
import com.withus.withmebe.gathering.dto.response.AddGatheringResponse;
import com.withus.withmebe.gathering.dto.response.DeleteGatheringResponse;
import com.withus.withmebe.gathering.dto.response.GetGatheringResponse;
import com.withus.withmebe.gathering.dto.response.SetGatheringResponse;
import com.withus.withmebe.gathering.service.GatheringService;
import com.withus.withmebe.gathering.Type.GatheringType;
import com.withus.withmebe.security.anotation.CurrentMemberId;
import jakarta.validation.Valid;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gathering")
public class GatheringController {

  private final GatheringService gatheringService;

  @PostMapping()
  public ResponseEntity<AddGatheringResponse> addGathering(@CurrentMemberId long currentMemberId,
      @Valid @RequestBody AddGatheringRequest addGatheringRequest) {
    return ResponseEntity.ok(
        gatheringService.createGathering(currentMemberId, addGatheringRequest));
  }

  @PostMapping("/image/{gathering}")
  public ResponseEntity<SetGatheringResponse> addGathering(@PathVariable long gathering,
      @RequestParam(value = "mainImg", required = false) MultipartFile mainImg,
      @RequestParam(value = "subImg1", required = false) MultipartFile subImg1,
      @RequestParam(value = "subImg2", required = false) MultipartFile subImg2,
      @RequestParam(value = "subImg3", required = false) MultipartFile subImg3) throws IOException {
    return ResponseEntity.ok(
        gatheringService.createGathering(gathering, mainImg, subImg1,
            subImg2, subImg3));
  }

  @GetMapping("/list")
  public ResponseEntity<Page<GetGatheringResponse>> getGatheringList(
      @PageableDefault(sort = "createdDttm", direction = Direction.DESC) Pageable pageable,
      @RequestParam GatheringType range) {
    return ResponseEntity.ok(gatheringService.readGatheringList(range, pageable));
  }

  @GetMapping("/mylist")
  public ResponseEntity<Page<GetGatheringResponse>> getGatheringMyList(
      @CurrentMemberId long currentMemberId,
      @PageableDefault(sort = "createdDttm", direction = Direction.DESC) Pageable pageable) {
    return ResponseEntity.ok(gatheringService.readGatheringMyList(currentMemberId, pageable));
  }

  @GetMapping("/{gatheringId}")
  public ResponseEntity<GetGatheringResponse> getGathering(@PathVariable long gatheringId) {
    return ResponseEntity.ok(gatheringService.readGathering(gatheringId));
  }

  @PutMapping("/{gatheringId}")
  public ResponseEntity<SetGatheringResponse> setGathering(@CurrentMemberId long currentMemberId,
      @PathVariable long gatheringId, @Valid @RequestBody SetGatheringRequest setGatheringRequest) {
    return ResponseEntity.ok(
        gatheringService.updateGathering(currentMemberId, gatheringId, setGatheringRequest));
  }

  @PutMapping("/image/{gathering}")
  public ResponseEntity<SetGatheringResponse> setGathering(@PathVariable long gathering,
      @RequestParam(value = "mainImg", required = false) MultipartFile mainImg,
      @RequestParam(value = "subImg1", required = false) MultipartFile subImg1,
      @RequestParam(value = "subImg2", required = false) MultipartFile subImg2,
      @RequestParam(value = "subImg3", required = false) MultipartFile subImg3) throws IOException {
    return ResponseEntity.ok(
        gatheringService.updateGathering(gathering, mainImg, subImg1,
            subImg2, subImg3));
  }

  @DeleteMapping("/{gatheringId}")
  public ResponseEntity<DeleteGatheringResponse> removeGathering(@CurrentMemberId long currentMemberId,
      @PathVariable long gatheringId) {
    return ResponseEntity.ok(gatheringService.deleteGathering(currentMemberId, gatheringId));
  }
}
