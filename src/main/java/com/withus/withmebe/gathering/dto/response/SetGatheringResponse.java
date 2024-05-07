package com.withus.withmebe.gathering.dto.response;


import com.withus.withmebe.gathering.Type.GatheringType;
import com.withus.withmebe.gathering.Type.ParticipantSelectionMethod;
import com.withus.withmebe.gathering.Type.ParticipantsType;
import com.withus.withmebe.gathering.entity.Gathering;
import jakarta.validation.constraints.NotBlank;
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
public class SetGatheringResponse {
    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotNull
    private GatheringType gatheringType;

    private Long maximumParticipant;

    @NotNull
    private LocalDateTime startDttm;

    @NotNull
    private LocalDateTime endDttm;

    @NotBlank
    private String category;

    @NotNull
    private LocalDateTime applicationDeadLine;

    @NotBlank
    private String address;

    @NotBlank
    private String detailedAddress;

    @NotBlank
    private String location;

    @NotBlank
    private String mainImg;

    @NotNull
    private ParticipantsType participantsType;

    @NotNull
    private Long fee;

    @NotNull
    private ParticipantSelectionMethod participantSelectionMethod;

    public static SetGatheringResponse toResponse(Gathering gathering) {
        return builder()
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
