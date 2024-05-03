package com.withus.withmebe.gathering.controller;

import com.withus.withmebe.gathering.dto.request.AddGatheringRequest;
import com.withus.withmebe.gathering.entity.Gathering;
import com.withus.withmebe.gathering.service.GatheringService;
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
    public ResponseEntity<?> getGatheringList() {
        System.out.println("호출 테스트");
        return null;
    }

    @PutMapping("/{gatheringId}")
    public ResponseEntity<?> setGathering(@PathVariable long gatheringId) {

        return null;
    }

    @GetMapping("/{gatheringId}")
    public ResponseEntity<?> getGathering(@PathVariable long gatheringId) {

        return null;
    }

    @DeleteMapping("/cancel/{gatheringId}")
    public ResponseEntity<?> removeGathering(@PathVariable long gatheringId) {

        return null;
    }
}
