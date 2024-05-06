package com.withus.withmebe.search.document;

import com.withus.withmebe.gathering.Type.GatheringType;
import com.withus.withmebe.gathering.Type.ParticipantSelectionMethod;
import com.withus.withmebe.gathering.Type.ParticipantsType;
import com.withus.withmebe.gathering.Type.Status;
import java.time.LocalDateTime;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

@Builder
@Document(indexName = "gathering")
@Setting(settingPath = "/settings.json")
public record GatheringSearch(

    @Id
    @Field(name = "gathering_id")
    Long id,

    String nickName,
    String title,

    @Field(type = FieldType.Keyword)
    GatheringType gatheringType,
    Long maximumParticipant,
    LocalDateTime startDttm,
    LocalDateTime endDttm,
    LocalDateTime applicationDeadLine,

    String address,
    String detailedAddress,
    String mainImg,

    @Field(type = FieldType.Keyword)
    ParticipantsType participantsType,
    Long fee,
    ParticipantSelectionMethod participantSelectionMethod,
    Long likeCount,

    @Field(type = FieldType.Date)
    LocalDateTime createdDttm,
    @Field(type = FieldType.Date)
    LocalDateTime deletedDttm,
    @Field(type = FieldType.Keyword)
    Status status
) {

}
