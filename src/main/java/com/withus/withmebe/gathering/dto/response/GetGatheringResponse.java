package com.withus.withmebe.gathering.dto.response;


import com.withus.withmebe.gathering.Type.GatheringType;
import com.withus.withmebe.gathering.Type.ParticipantSelectionMethod;
import com.withus.withmebe.gathering.Type.ParticipantsType;
import com.withus.withmebe.gathering.entity.Gathering;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
public class GetGatheringResponse {
    private Long memberId;

    @NotNull
    private String title;

    private String content;

    @NotNull
    private GatheringType gatheringType;

    private Long maximumParticipant;

    @NotNull
    private LocalDateTime startDttm;

    @NotNull
    private LocalDateTime endDttm;

    @NotNull
    private String category;

    @NotNull
    private LocalDateTime applicationDeadLine;

    @NotNull
    private String address;

    @NotNull
    private String detailedAddress;

    private String location;

    @NotNull
    private String mainImg;

    @NotNull
    private ParticipantsType participantsType;

    @NotNull
    private Long fee;

    @NotNull
    private ParticipantSelectionMethod participantSelectionMethod;

    public static GetGatheringResponse toEntity(Gathering gathering) {
        return   builder()
                .memberId(gathering.getMemberId())
                .title(gathering.getTitle())
                .content(gathering.getContent())
                .gatheringType(gathering.getGatheringType())
                .maximumParticipant(gathering.getMaximumParticipant())
                .startDttm(gathering.getStartDttm())
                .endDttm(gathering.getEndDttm())
                .category(gathering.getCategory())
                .applicationDeadLine(gathering.getApplicationDeadLine())
                .address(gathering.getAddress())
                .detailedAddress(gathering.getDetailedAddress())
                .location(gathering.getLocation())
                .mainImg(gathering.getMainImg())
                .participantsType(gathering.getParticipantsType())
                .fee(gathering.getFee())
                .participantSelectionMethod(gathering.getParticipantSelectionMethod())
                .build();
    }
}
