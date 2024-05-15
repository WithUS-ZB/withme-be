package com.withus.withmebe.search.document;

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
    @Field(name = "member_id")
    Long memberId,
    @Field(name = "nick_name")
    String nickName,
    @Field(name = "profile_img")
    String profileImg,
    @Field(type = FieldType.Text, analyzer = "korean")
    String title,
    String content,
    @Field(name = "gathering_type")
    String gatheringType,
    @Field(name = "maximum_participant")
    Long maximumParticipant,
    LocalDateTime day,
    LocalDateTime time,
    @Field(name = "recruitment_start_dt")
    LocalDateTime recruitmentStartDt,
    @Field(name = "recruitment_end_dt")
    LocalDateTime recruitmentEndDt,
    String category,
    String address,
    @Field(name = "main_img")
    String mainImg,
    @Field(name = "participants_type")
    String participantsType,
    Long fee,
    @Field(name = "participant_selection_method")
    String participantSelectionMethod,
    @Field(name = "like_count")
    Long likeCount,
    @Field(name = "created_dttm")
    LocalDateTime createdDttm,
    @Field(name = "deleted_dttm")
    LocalDateTime deletedDttm,
    String status
) {

}
