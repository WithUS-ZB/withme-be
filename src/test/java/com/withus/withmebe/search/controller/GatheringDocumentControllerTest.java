package com.withus.withmebe.search.controller;

import static com.withus.withmebe.search.util.GatheringDocumentProvider.getStubbedGatheringDocument;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.withus.withmebe.search.document.GatheringDocument;
import com.withus.withmebe.search.service.GatheringDocumentService;
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
import org.springframework.data.domain.PageRequest;
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

  private final String baseUrl = "/api/search/gathering";

  @Test
  @WithMockUser
  void successToSearchGatheringDocumentsByTitle() throws Exception {
    //given
    GatheringDocument gatheringDocument1 = getStubbedGatheringDocument(1);
    GatheringDocument gatheringDocument2 = getStubbedGatheringDocument(2);
    Pageable pageable = PageRequest.of(0, 10);
    String query = "제목";

    given(gatheringDocumentService.searchGatheringDocumentsByTitleAndStatus(anyString(), anyString(), any()))
        .willReturn(new PageImpl<GatheringDocument>(List.of(gatheringDocument1, gatheringDocument2),
            pageable, 2));
    //when
    //then
    mockMvc.perform(get(baseUrl + "/title?query=" + query)
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.totalElements").value(2))
        .andExpect(jsonPath("$.totalPages").value(1))
        .andExpect(jsonPath("$.pageable.pageNumber").value(0))
        .andExpect(jsonPath("$.content[0].id").value(gatheringDocument1.id()))
        .andExpect(jsonPath("$.content[0].nickName").value(gatheringDocument1.nickName()))
        .andExpect(jsonPath("$.content[0].title").value(gatheringDocument1.title()))
        .andExpect(jsonPath("$.content[0].gatheringType").value(gatheringDocument1.gatheringType()))
        .andExpect(jsonPath("$.content[0].maximumParticipant").value(gatheringDocument1.maximumParticipant()))
        .andExpect(jsonPath("$.content[0].address").value(gatheringDocument1.address()))
        .andExpect(jsonPath("$.content[0].detailedAddress").value(gatheringDocument1.detailedAddress()))
        .andExpect(jsonPath("$.content[0].mainImg").value(gatheringDocument1.mainImg()))
        .andExpect(jsonPath("$.content[0].participantsType").value(gatheringDocument1.participantsType()))
        .andExpect(jsonPath("$.content[0].fee").value(gatheringDocument1.fee()))
        .andExpect(jsonPath("$.content[0].participantSelectionMethod").value(gatheringDocument1.participantSelectionMethod()))
        .andExpect(jsonPath("$.content[0].likeCount").value(gatheringDocument1.likeCount()))
        .andExpect(jsonPath("$.content[0].startDttm").value(gatheringDocument1.startDttm().format(
            DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
        .andExpect(jsonPath("$.content[0].endDttm").value(gatheringDocument1.endDttm().format(
            DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
        .andExpect(jsonPath("$.content[0].applicationDeadLine").value(gatheringDocument1.applicationDeadLine().format(
            DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
        .andExpect(jsonPath("$.content[0].createdDttm").value(gatheringDocument1.createdDttm().format(
            DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
        .andExpect(jsonPath("$.content[0].status").value(gatheringDocument1.status()))
        .andExpect(jsonPath("$.content[1].id").value(gatheringDocument2.id()))
        .andExpect(jsonPath("$.content[1].nickName").value(gatheringDocument2.nickName()))
        .andExpect(jsonPath("$.content[1].title").value(gatheringDocument2.title()))
        .andExpect(jsonPath("$.content[1].gatheringType").value(gatheringDocument2.gatheringType()))
        .andExpect(jsonPath("$.content[1].maximumParticipant").value(gatheringDocument2.maximumParticipant()))
        .andExpect(jsonPath("$.content[1].address").value(gatheringDocument2.address()))
        .andExpect(jsonPath("$.content[1].detailedAddress").value(gatheringDocument2.detailedAddress()))
        .andExpect(jsonPath("$.content[1].mainImg").value(gatheringDocument2.mainImg()))
        .andExpect(jsonPath("$.content[1].participantsType").value(gatheringDocument2.participantsType()))
        .andExpect(jsonPath("$.content[1].fee").value(gatheringDocument2.fee()))
        .andExpect(jsonPath("$.content[1].participantSelectionMethod").value(gatheringDocument2.participantSelectionMethod()))
        .andExpect(jsonPath("$.content[1].likeCount").value(gatheringDocument2.likeCount()))
        .andExpect(jsonPath("$.content[1].status").value(gatheringDocument2.status()))
        .andExpect(jsonPath("$.content[1].startDttm").value(gatheringDocument2.startDttm().format(
            DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
        .andExpect(jsonPath("$.content[1].endDttm").value(gatheringDocument2.endDttm().format(
            DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
        .andExpect(jsonPath("$.content[1].applicationDeadLine").value(gatheringDocument2.applicationDeadLine().format(
            DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
        .andExpect(jsonPath("$.content[1].createdDttm").value(gatheringDocument2.createdDttm().format(
            DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
  }

  @Test
  @WithMockUser
  void failToSearchToSearchGatheringDocumentsByBadRequest() throws Exception {
    //given
    //when
    //then
    mockMvc.perform(get(baseUrl + "/title?")
        .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isBadRequest());
  }
}