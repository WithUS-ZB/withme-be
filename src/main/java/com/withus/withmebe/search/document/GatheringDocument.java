package com.withus.withmebe.search.document;

import com.withus.withmebe.gathering.Type.ParticipantSelectionMethod;
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
public record GatheringDocument(
    @Id
    @Field(name = "gathering_id")
    Long id,
    @Field(name = "nickname")
    String nickName,
    @Field(type = FieldType.Text, analyzer = "korean")
    String title,
    @Field(name = "gatheringtype")
    String gatheringType,
    @Field(name = "maximumparticipant")
    Long maximumParticipant,
    @Field(name = "startdttm")
    LocalDateTime startDttm,
    @Field(name = "enddttm")
    LocalDateTime endDttm,
    @Field(name = "applicationdeadline")
    LocalDateTime applicationDeadLine,
    String address,
    @Field(name = "detailedaddress")
    String detailedAddress,
    @Field(name = "mainimg")
    String mainImg,
    @Field(name = "participantstype")
    String participantsType,
    Long fee,
    @Field(name = "participantselectionmethod")
    String participantSelectionMethod,
    @Field(name = "likecount")
    Long likeCount,
    @Field(name = "createddttm")
    LocalDateTime createdDttm,
    @Field(name = "deleteddttm")
    LocalDateTime deletedDttm,
    String status
) {

}
