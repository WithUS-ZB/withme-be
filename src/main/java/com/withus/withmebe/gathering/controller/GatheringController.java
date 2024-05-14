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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gathering")
public class GatheringController {

    private final GatheringService gatheringService;

    @PostMapping()
    public ResponseEntity<AddGatheringResponse> addGathering(@CurrentMemberId long currentMemberId,
                                                             @Valid @RequestBody AddGatheringRequest addGatheringRequest) {
        return ResponseEntity.ok(gatheringService.createGathering(currentMemberId, addGatheringRequest));
    }

    @GetMapping("/list")
    public ResponseEntity<List<GetGatheringResponse>> getGatheringList() {
        return ResponseEntity.ok(gatheringService.readGatheringList());
    }

    @PutMapping("/{gatheringId}")
    public ResponseEntity<SetGatheringResponse> setGathering(@CurrentMemberId long currentMemberId,
                                                             @PathVariable long gatheringId,
                                                             @Valid @RequestBody SetGatheringRequest setGatheringRequest) {
        return ResponseEntity.ok(gatheringService.updateGathering(currentMemberId, gatheringId, setGatheringRequest));
    }

    @GetMapping("/{gatheringId}")
    public ResponseEntity<GetGatheringResponse> getGathering(@PathVariable long gatheringId) {
        return ResponseEntity.ok(gatheringService.readGathering(gatheringId));
    }

    @DeleteMapping("/{gatheringId}")
    public ResponseEntity<DeleteGatheringResponse> removeGathering(@CurrentMemberId long currentMemberId,
                                                                   @PathVariable long gatheringId) {
        return ResponseEntity.ok(gatheringService.deleteGathering(currentMemberId, gatheringId));
    }
}
