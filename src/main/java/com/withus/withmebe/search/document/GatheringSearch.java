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
@Document(indexName = "idx-gathering")
@Setting(settingPath = "/elasticsearch/settings.json")
public record GatheringSearch(

    @Id
    @Field(name = "gathering_id", type = FieldType.Long)
    Long id,

    @Field(type = FieldType.Text)
    String memberNickName,
    @Field(type = FieldType.Text)
    String title,
    @Field(type = FieldType.Text)
    String content,

    @Field(type = FieldType.Keyword)
    GatheringType gatheringType,
    @Field(type = FieldType.Long)
    Long maximumParticipant,

    @Field(type = FieldType.Date)
    LocalDateTime startDttm,
    @Field(type = FieldType.Date)
    LocalDateTime endDttm,
    @Field(type = FieldType.Keyword)
    String category,
    @Field(type = FieldType.Date)
    LocalDateTime applicationDeadLine,

    @Field(type = FieldType.Text)
    String address,
    @Field(type = FieldType.Text)
    String detailedAddress,
    @Field(type = FieldType.Object)
    String location,

    String mainImg,

    @Field(type = FieldType.Keyword)
    ParticipantsType participantsType,
    Long fee,

    @Field(type = FieldType.Keyword)
    ParticipantSelectionMethod participantSelectionMethod,
    Long likeCount,

    @Field(type = FieldType.Keyword)
    Status status,

    @Field(type = FieldType.Date)
    LocalDateTime createdDttm,
    @Field(type = FieldType.Date)
    LocalDateTime updatedDttm
) {

}
