package com.withus.withmebe.gathering.service;

import com.withus.withmebe.gathering.dto.request.AddGatheringRequest;
import com.withus.withmebe.gathering.entity.Gathering;
import com.withus.withmebe.gathering.repository.GatheringRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class GatheringService {

    private final GatheringRepository gatheringRepository;

    @Transactional
    public Gathering createGathering(long memberId, AddGatheringRequest addGatheringRequest) {
        return gatheringRepository.save(addGatheringRequest.toEntity(memberId));
    }
}
