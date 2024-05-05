package com.withus.withmebe.gathering.controller;

import com.withus.withmebe.gathering.dto.request.AddGatheringRequest;
import com.withus.withmebe.gathering.entity.Gathering;
import com.withus.withmebe.gathering.service.GatheringService;
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

    @PostMapping("/{memberId}")
    public ResponseEntity<Gathering> addGathering(@PathVariable long memberId,
                                                  @RequestBody AddGatheringRequest addGatheringRequest) {
        return ResponseEntity.ok(gatheringService.createGathering(memberId, addGatheringRequest));
    }

    @GetMapping("/list")
    public ResponseEntity<List<Gathering>> getGatheringList() {
        return ResponseEntity.ok(gatheringService.readGatheringList());
    }

    @PutMapping("/{memberId}/{gatheringId}")
    public ResponseEntity<Gathering> setGathering(@PathVariable long memberId, @PathVariable long gatheringId,
                                                  @RequestBody AddGatheringRequest addGatheringRequest) {
        return ResponseEntity.ok(gatheringService.updateGathering(memberId, gatheringId, addGatheringRequest));
    }

    @GetMapping("/{gatheringId}")
    public ResponseEntity<Gathering> getGathering(@PathVariable long gatheringId) {
        return ResponseEntity.ok(gatheringService.readGathering(gatheringId));
    }

    @DeleteMapping("/cancel/{memberId}/{gatheringId}")
    public ResponseEntity<String> removeGathering(@PathVariable long memberId, @PathVariable long gatheringId) {
        gatheringService.deleteGathering(memberId, gatheringId);
        return ResponseEntity.ok("200");
    }
}
