package com.withus.withmebe.gathering.service;

import static com.withus.withmebe.common.exception.ExceptionCode.ENTITY_NOT_FOUND;

import com.withus.withmebe.common.exception.CustomException;
import com.withus.withmebe.common.exception.ExceptionCode;
import com.withus.withmebe.gathering.dto.request.AddGatheringRequest;
import com.withus.withmebe.gathering.dto.response.GetGatheringResponse;
import com.withus.withmebe.gathering.entity.Gathering;
import com.withus.withmebe.gathering.repository.GatheringRepository;
import com.withus.withmebe.member.repository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GatheringService {

    private final GatheringRepository gatheringRepository;
    private final MemberRepository memberRepository;

    public Gathering createGathering(long currentMemberId, AddGatheringRequest addGatheringRequest) {
        getRequesterMember(currentMemberId);
        return gatheringRepository.save(addGatheringRequest.toEntity(currentMemberId));
    }

    public Page<GetGatheringResponse> readGatheringList(Pageable pageable) {
        Pageable adjustedPageable = adjustPageable(pageable);
        Page<Gathering> gatherings = gatheringRepository.findAll(adjustedPageable);
        return gatherings.map(GetGatheringResponse::toEntity);
    }

    public Gathering updateGathering(long currentMemberId, long gatheringId, AddGatheringRequest addGatheringRequest) {
        Gathering gathering = getGathering(currentMemberId, gatheringId);
        updateGatheringFields(addGatheringRequest, gathering);
        return gatheringRepository.save(gathering);
    }

    public Gathering readGathering(Long gatheringId) {
        return getRequesterGathering(gatheringId);
    }

    public void deleteGathering(long currentMemberId, long gatheringId) {
        getGathering(currentMemberId, gatheringId);
        gatheringRepository.deleteById(gatheringId);
    }

    private Pageable adjustPageable(Pageable pageable) {
        int size = Math.max(pageable.getPageSize(), 1);
        int page = Math.max(pageable.getPageNumber(), 0);
        return PageRequest.of(page, size);
    }

    private Gathering getGathering(long memberId, long gatheringId) {
        Gathering gathering = getRequesterGathering(gatheringId);
        if (memberId != gathering.getMemberId()) {
            throw new CustomException(ExceptionCode.AUTHORIZATION_ISSUE);
        }
        return gathering;
    }

    private Gathering getRequesterGathering(long gatheringId) {
        return gatheringRepository.findById(gatheringId).orElseThrow(() -> new CustomException(ENTITY_NOT_FOUND));
    }

    private void getRequesterMember(long memberId) {
        memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ENTITY_NOT_FOUND));
    }

    private void updateGatheringFields(AddGatheringRequest addGatheringRequest, Gathering gathering) {
        gathering.setTitle(addGatheringRequest.getTitle());
        gathering.setContent(addGatheringRequest.getContent());
        gathering.setGatheringType(addGatheringRequest.getGatheringType());
        gathering.setMaximumParticipant(addGatheringRequest.getMaximumParticipant());
        gathering.setStartDttm(addGatheringRequest.getStartDttm());
        gathering.setEndDttm(addGatheringRequest.getEndDttm());
        gathering.setApplicationDeadLine(addGatheringRequest.getApplicationDeadLine());
        gathering.setAddress(addGatheringRequest.getAddress());
        gathering.setDetailedAddress(addGatheringRequest.getDetailedAddress());
        gathering.setLocation(addGatheringRequest.getLocation());
        gathering.setMainImg(addGatheringRequest.getMainImg());
        gathering.setParticipantsType(addGatheringRequest.getParticipantsType());
        gathering.setCategory(addGatheringRequest.getCategory());
        gathering.setFee(addGatheringRequest.getFee());
        gathering.setParticipantSelectionMethod(addGatheringRequest.getParticipantSelectionMethod());
    }
}
