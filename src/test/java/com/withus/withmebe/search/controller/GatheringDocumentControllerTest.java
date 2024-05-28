package com.withus.withmebe.search.controller;

import static org.mockito.ArgumentMatchers.argThat;
import static util.objectprovider.GatheringDocumentProvider.getStubbedGatheringDocument;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.withus.withmebe.search.dto.GatheringSearchResponse;
import com.withus.withmebe.search.service.GatheringDocumentService;
import com.withus.withmebe.search.type.SearchOption;
import com.withus.withmebe.security.jwt.filter.JwtAuthenticationFilter;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = GatheringDocumentController.class, excludeFilters = {
    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)})
@MockBean(JpaMetamodelMappingContext.class)
class GatheringDocumentControllerTest {

  @MockBean
  private GatheringDocumentService gatheringDocumentService;

  @Autowired
  private MockMvc mockMvc;

  private final String BASE_URL = "/api/search/gathering";
  private final String TITLE = "title=제목";
  private final String RANGE = "range=all";
  private final String OPTION = "option=all";

  @Test
  @WithMockUser
  void successToSearchGatheringDocumentsByTitle() throws Exception {
    //given
    GatheringSearchResponse gatheringSearchResponse1 =
        getStubbedGatheringDocument(1).toGatheringSearchResponse();
    GatheringSearchResponse gatheringSearchResponse2 =
        getStubbedGatheringDocument(2).toGatheringSearchResponse();

    given(gatheringDocumentService.searchGatheringDocumentsByTitle(any(SearchOption.class),
        anyString(), any(
            Pageable.class), any(SearchOption.class)))
        .willReturn(new PageImpl<GatheringSearchResponse>(
            List.of(gatheringSearchResponse1, gatheringSearchResponse2),
            Pageable.ofSize(10), 2));
    //when
    //then
    mockMvc.perform(get(BASE_URL + "/title?" + TITLE + "&" + RANGE + "&" + OPTION)
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.totalElements").value(2))
        .andExpect(jsonPath("$.totalPages").value(1))
        .andExpect(jsonPath("$.pageable.pageNumber").value(0))
        .andExpect(jsonPath("$.content[0].gatheringId").value(gatheringSearchResponse1.gatheringId()))
        .andExpect(jsonPath("$.content[0].memberId").value(gatheringSearchResponse1.memberId()))
        .andExpect(jsonPath("$.content[0].nickName").value(gatheringSearchResponse1.nickName()))
        .andExpect(jsonPath("$.content[0].profileImg").value(gatheringSearchResponse1.profileImg()))
        .andExpect(jsonPath("$.content[0].title").value(gatheringSearchResponse1.title()))
        .andExpect(jsonPath("$.content[0].content").value(gatheringSearchResponse1.content()))
        .andExpect(jsonPath("$.content[0].gatheringType").value(gatheringSearchResponse1.gatheringType()))
        .andExpect(jsonPath("$.content[0].day").value(gatheringSearchResponse1.day().format(DateTimeFormatter.ISO_LOCAL_DATE)))
        .andExpect(jsonPath("$.content[0].time").value(gatheringSearchResponse1.time().format(DateTimeFormatter.ISO_LOCAL_TIME)))
        .andExpect(jsonPath("$.content[0].recruitmentStartDt").value(gatheringSearchResponse1.recruitmentStartDt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
        .andExpect(jsonPath("$.content[0].recruitmentEndDt").value(gatheringSearchResponse1.recruitmentEndDt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
        .andExpect(jsonPath("$.content[0].category").value(gatheringSearchResponse1.category()))
        .andExpect(jsonPath("$.content[0].address").value(gatheringSearchResponse1.address()))
        .andExpect(jsonPath("$.content[0].mainImg").value(gatheringSearchResponse1.mainImg()))
        .andExpect(jsonPath("$.content[0].participantsType").value(gatheringSearchResponse1.participantsType()))
        .andExpect(jsonPath("$.content[0].fee").value(gatheringSearchResponse1.fee()))
        .andExpect(jsonPath("$.content[0].participantSelectionMethod").value(gatheringSearchResponse1.participantSelectionMethod()))
        .andExpect(jsonPath("$.content[0].likeCount").value(gatheringSearchResponse1.likeCount()))
        .andExpect(jsonPath("$.content[0].createdDttm").value(gatheringSearchResponse1.createdDttm().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
        .andExpect(jsonPath("$.content[0].status").value(gatheringSearchResponse1.status()))
        .andExpect(jsonPath("$.content[1].gatheringId").value(gatheringSearchResponse2.gatheringId()))
        .andExpect(jsonPath("$.content[1].memberId").value(gatheringSearchResponse2.memberId()))
        .andExpect(jsonPath("$.content[1].nickName").value(gatheringSearchResponse2.nickName()))
        .andExpect(jsonPath("$.content[1].profileImg").value(gatheringSearchResponse2.profileImg()))
        .andExpect(jsonPath("$.content[1].title").value(gatheringSearchResponse2.title()))
        .andExpect(jsonPath("$.content[1].content").value(gatheringSearchResponse2.content()))
        .andExpect(jsonPath("$.content[1].gatheringType").value(gatheringSearchResponse2.gatheringType()))
        .andExpect(jsonPath("$.content[1].day").value(gatheringSearchResponse2.day().format(DateTimeFormatter.ISO_LOCAL_DATE)))
        .andExpect(jsonPath("$.content[1].time").value(gatheringSearchResponse2.time().format(DateTimeFormatter.ISO_LOCAL_TIME)))
        .andExpect(jsonPath("$.content[1].recruitmentStartDt").value(gatheringSearchResponse2.recruitmentStartDt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
        .andExpect(jsonPath("$.content[1].recruitmentEndDt").value(gatheringSearchResponse2.recruitmentEndDt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
        .andExpect(jsonPath("$.content[1].category").value(gatheringSearchResponse2.category()))
        .andExpect(jsonPath("$.content[1].address").value(gatheringSearchResponse2.address()))
        .andExpect(jsonPath("$.content[1].mainImg").value(gatheringSearchResponse2.mainImg()))
        .andExpect(jsonPath("$.content[1].participantsType").value(gatheringSearchResponse2.participantsType()))
        .andExpect(jsonPath("$.content[1].fee").value(gatheringSearchResponse2.fee()))
        .andExpect(jsonPath("$.content[1].participantSelectionMethod").value(gatheringSearchResponse2.participantSelectionMethod()))
        .andExpect(jsonPath("$.content[1].likeCount").value(gatheringSearchResponse2.likeCount()))
        .andExpect(jsonPath("$.content[1].createdDttm").value(gatheringSearchResponse2.createdDttm().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
        .andExpect(jsonPath("$.content[1].status").value(gatheringSearchResponse2.status()))
        ;
  }

  @Test
  @WithMockUser
  void failToSearchGatheringDocumentsByBadRequestNoRange() throws Exception {
    //given
    //when
    //then
    mockMvc.perform(get(BASE_URL + "/title?" + TITLE + "&" + OPTION)
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser
  void failToSearchGatheringDocumentsByBadRequestBadRange() throws Exception {
    //given
    //when
    //then
    mockMvc.perform(get(BASE_URL + "/title?" + TITLE + "&" + OPTION + "&range=something")
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser
  void failToSearchGatheringDocumentsByBadRequestNoOption() throws Exception {
    //given
    //when
    //then
    mockMvc.perform(get(BASE_URL + "/title?" + TITLE + "&" + RANGE)
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser
  void failToSearchGatheringDocumentsByBadRequestBadOption() throws Exception {
    //given
    //when
    //then
    mockMvc.perform(get(BASE_URL + "/title?" + TITLE + "&" + RANGE + "&option=something")
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser
  void successToSearchGatheringDocumentsWhenNoTitle() throws Exception {
    //given
    GatheringSearchResponse gatheringSearchResponse1 =
        getStubbedGatheringDocument(1).toGatheringSearchResponse();
    GatheringSearchResponse gatheringSearchResponse2 =
        getStubbedGatheringDocument(2).toGatheringSearchResponse();

    given(gatheringDocumentService.searchGatheringDocumentsByTitle(any(SearchOption.class),
        argThat(title -> title == null || title.isEmpty()), any(Pageable.class), any(SearchOption.class)))
        .willReturn(new PageImpl<GatheringSearchResponse>(
            List.of(gatheringSearchResponse1, gatheringSearchResponse2),
            Pageable.ofSize(10), 2));
    //when
    //then
    mockMvc.perform(get(BASE_URL + "/title?" + RANGE + "&" + OPTION)
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.totalElements").value(2))
        .andExpect(jsonPath("$.totalPages").value(1))
        .andExpect(jsonPath("$.pageable.pageNumber").value(0))
        .andExpect(jsonPath("$.content[0].gatheringId").value(gatheringSearchResponse1.gatheringId()))
        .andExpect(jsonPath("$.content[0].memberId").value(gatheringSearchResponse1.memberId()))
        .andExpect(jsonPath("$.content[0].nickName").value(gatheringSearchResponse1.nickName()))
        .andExpect(jsonPath("$.content[0].profileImg").value(gatheringSearchResponse1.profileImg()))
        .andExpect(jsonPath("$.content[0].title").value(gatheringSearchResponse1.title()))
        .andExpect(jsonPath("$.content[0].content").value(gatheringSearchResponse1.content()))
        .andExpect(jsonPath("$.content[0].gatheringType").value(gatheringSearchResponse1.gatheringType()))
        .andExpect(jsonPath("$.content[0].day").value(gatheringSearchResponse1.day().format(DateTimeFormatter.ISO_LOCAL_DATE)))
        .andExpect(jsonPath("$.content[0].time").value(gatheringSearchResponse1.time().format(DateTimeFormatter.ISO_LOCAL_TIME)))
        .andExpect(jsonPath("$.content[0].recruitmentStartDt").value(gatheringSearchResponse1.recruitmentStartDt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
        .andExpect(jsonPath("$.content[0].recruitmentEndDt").value(gatheringSearchResponse1.recruitmentEndDt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
        .andExpect(jsonPath("$.content[0].category").value(gatheringSearchResponse1.category()))
        .andExpect(jsonPath("$.content[0].address").value(gatheringSearchResponse1.address()))
        .andExpect(jsonPath("$.content[0].mainImg").value(gatheringSearchResponse1.mainImg()))
        .andExpect(jsonPath("$.content[0].participantsType").value(gatheringSearchResponse1.participantsType()))
        .andExpect(jsonPath("$.content[0].fee").value(gatheringSearchResponse1.fee()))
        .andExpect(jsonPath("$.content[0].participantSelectionMethod").value(gatheringSearchResponse1.participantSelectionMethod()))
        .andExpect(jsonPath("$.content[0].likeCount").value(gatheringSearchResponse1.likeCount()))
        .andExpect(jsonPath("$.content[0].createdDttm").value(gatheringSearchResponse1.createdDttm().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
        .andExpect(jsonPath("$.content[0].status").value(gatheringSearchResponse1.status()))
        .andExpect(jsonPath("$.content[1].gatheringId").value(gatheringSearchResponse2.gatheringId()))
        .andExpect(jsonPath("$.content[1].memberId").value(gatheringSearchResponse2.memberId()))
        .andExpect(jsonPath("$.content[1].nickName").value(gatheringSearchResponse2.nickName()))
        .andExpect(jsonPath("$.content[1].profileImg").value(gatheringSearchResponse2.profileImg()))
        .andExpect(jsonPath("$.content[1].title").value(gatheringSearchResponse2.title()))
        .andExpect(jsonPath("$.content[1].content").value(gatheringSearchResponse2.content()))
        .andExpect(jsonPath("$.content[1].gatheringType").value(gatheringSearchResponse2.gatheringType()))
        .andExpect(jsonPath("$.content[1].day").value(gatheringSearchResponse2.day().format(DateTimeFormatter.ISO_LOCAL_DATE)))
        .andExpect(jsonPath("$.content[1].time").value(gatheringSearchResponse2.time().format(DateTimeFormatter.ISO_LOCAL_TIME)))
        .andExpect(jsonPath("$.content[1].recruitmentStartDt").value(gatheringSearchResponse2.recruitmentStartDt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
        .andExpect(jsonPath("$.content[1].recruitmentEndDt").value(gatheringSearchResponse2.recruitmentEndDt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
        .andExpect(jsonPath("$.content[1].category").value(gatheringSearchResponse2.category()))
        .andExpect(jsonPath("$.content[1].address").value(gatheringSearchResponse2.address()))
        .andExpect(jsonPath("$.content[1].mainImg").value(gatheringSearchResponse2.mainImg()))
        .andExpect(jsonPath("$.content[1].participantsType").value(gatheringSearchResponse2.participantsType()))
        .andExpect(jsonPath("$.content[1].fee").value(gatheringSearchResponse2.fee()))
        .andExpect(jsonPath("$.content[1].participantSelectionMethod").value(gatheringSearchResponse2.participantSelectionMethod()))
        .andExpect(jsonPath("$.content[1].likeCount").value(gatheringSearchResponse2.likeCount()))
        .andExpect(jsonPath("$.content[1].createdDttm").value(gatheringSearchResponse2.createdDttm().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
        .andExpect(jsonPath("$.content[1].status").value(gatheringSearchResponse2.status()))
    ;
  }
}