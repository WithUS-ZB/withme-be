package com.withus.withmebe.gathering.service;

import com.withus.withmebe.common.exception.ExceptionCode;
import com.withus.withmebe.gathering.dto.request.AddGatheringRequest;
import com.withus.withmebe.gathering.entity.Gathering;
import com.withus.withmebe.gathering.repository.GatheringRepository;
import com.withus.withmebe.member.repository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GatheringService {

    private final GatheringRepository gatheringRepository;
    private final MemberRepository memberRepository;

    public Gathering createGathering(long memberId, AddGatheringRequest addGatheringRequest) {
        System.out.println(ExceptionCode.ENTITY_NOT_FOUND);
        memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException(ExceptionCode.ENTITY_NOT_FOUND.getMessage()));
        return gatheringRepository.save(addGatheringRequest.toEntity(memberId));
    }

    public List<Gathering> readGatheringList() {
        return gatheringRepository.findAll();
    }

    public Gathering updateGathering(long memberId, long gatheringId, AddGatheringRequest addGatheringRequest) {
        Gathering gathering = gatheringRepository.findById(gatheringId)
                .orElseThrow(() -> new IllegalArgumentException(ExceptionCode.ENTITY_NOT_FOUND.getMessage()));
        if (memberId != gathering.getMemberId()) {
            throw new IllegalArgumentException(ExceptionCode.AUTHORIZATION_ISSUE.getMessage());
        }
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
        return gatheringRepository.save(gathering);
    }

//    private Member readEditableGathering(long memberId) {
//        Member member = readComment(memberId);
//        System.out.println("여기");
//        if (member.getId() != getRequesterId()) {
//            throw new CustomException(AUTHORIZATION_ISSUE);
//        }
//        return member;
//    }
//
//    private Member readComment(long memberId) {
//        return memberRepository.findById(memberId)
//                .orElseThrow(() -> new CustomException(ENTITY_NOT_FOUND));
//    }
//
//    private long getRequesterId() {
//        String memberId = MySecurityUtil.getCustomUserDetails().getUsername();
//        return Long.parseLong(memberId);
//    }
}
