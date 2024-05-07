package com.withus.withmebe.gathering.controller;

import com.withus.withmebe.gathering.dto.request.AddGatheringRequest;
import com.withus.withmebe.gathering.dto.response.GetGatheringResponse;
import com.withus.withmebe.gathering.entity.Gathering;
import com.withus.withmebe.gathering.service.GatheringService;
import com.withus.withmebe.security.anotation.CurrentMemberId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gathering")
public class GatheringController {

    private final GatheringService gatheringService;

    @PostMapping()
    public ResponseEntity<Gathering> addGathering(@CurrentMemberId long currentMemberId,
                                                  @Valid @RequestBody AddGatheringRequest addGatheringRequest) {
        return ResponseEntity.ok(gatheringService.createGathering(currentMemberId, addGatheringRequest));
    }

    @GetMapping("/list")
    public ResponseEntity<Page<GetGatheringResponse>> getGatheringList(
            @PageableDefault(size = 10, sort = "createdDttm") Pageable pageable) {
        return ResponseEntity.ok(gatheringService.readGatheringList(pageable));
    }

    @PutMapping("/{gatheringId}")
    public ResponseEntity<Gathering> setGathering(@CurrentMemberId long currentMemberId, @PathVariable long gatheringId,
                                                  @Valid @RequestBody AddGatheringRequest addGatheringRequest) {
        return ResponseEntity.ok(gatheringService.updateGathering(currentMemberId, gatheringId, addGatheringRequest));
    }

    @GetMapping("/{gatheringId}")
    public ResponseEntity<Gathering> getGathering(@PathVariable long gatheringId) {
        return ResponseEntity.ok(gatheringService.readGathering(gatheringId));
    }

    @DeleteMapping("/{gatheringId}")
    public ResponseEntity<String> removeGathering(@CurrentMemberId long currentMemberId,
                                                  @PathVariable long gatheringId) {
        gatheringService.deleteGathering(currentMemberId, gatheringId);
        return ResponseEntity.ok("200");
    }
}
