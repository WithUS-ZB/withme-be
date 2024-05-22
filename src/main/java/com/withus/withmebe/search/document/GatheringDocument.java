package com.withus.withmebe.search.document;

import com.withus.withmebe.search.dto.GatheringSearchResponse;
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
    @Field(type = FieldType.Text, analyzer = "edge_ngram_analyzer", searchAnalyzer = "standard_analyzer", name = "ngram_title")
    String ngramTitle,
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
    public GatheringSearchResponse toGatheringSearchResponse() {
        return GatheringSearchResponse.builder()
            .gatheringId(this.id)
            .memberId(this.memberId)
            .nickName(this.nickName)
            .profileImg(this.profileImg)
            .title(this.title)
            .content(this.content)
            .gatheringType(this.gatheringType)
            .maximumParticipant(this.maximumParticipant)
            .day(this.day.toLocalDate())
            .time(this.time.toLocalTime())
            .recruitmentStartDt(this.recruitmentStartDt)
            .recruitmentEndDt(this.recruitmentEndDt)
            .category(this.category)
            .address(this.address)
            .mainImg(this.mainImg)
            .participantsType(this.participantsType)
            .fee(this.fee)
            .participantSelectionMethod(this.participantSelectionMethod)
            .likeCount(this.likeCount)
            .createdDttm(this.createdDttm)
            .status(this.status)
            .build();
    }
}
