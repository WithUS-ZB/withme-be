package com.withus.withmebe.gathering.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.withus.withmebe.common.exception.CustomException;
import com.withus.withmebe.common.exception.ExceptionCode;
import com.withus.withmebe.gathering.service.GatheringLikeService;
import com.withus.withmebe.security.jwt.filter.JwtAuthenticationFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import util.security.WithMockCustomUser;

@WebMvcTest(controllers = GatheringLikeController.class, excludeFilters = {
    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)})
@MockBean(JpaMetamodelMappingContext.class)
class GatheringLikeControllerTest {

  @MockBean
  private GatheringLikeService gatheringLikeService;

  @Autowired
  private MockMvc mockMvc;

  private static final String BASE_URL = "/api/gathering/like";
  private static final long GATHERING_ID = 1L;
  private static final long REQUESTER_ID = 2L;

  @Test
  @WithMockCustomUser(memberId = REQUESTER_ID)
  void successToUpdateLike() throws Exception {
    //given
    given(gatheringLikeService.doLike(REQUESTER_ID, GATHERING_ID))
        .willReturn(true);
    //when
    //then
    mockMvc.perform(put(BASE_URL + "?gatheringid=" + GATHERING_ID)
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isOk())
        .andExpect(content().string("true"));
  }

  @Test
  @WithMockCustomUser(memberId = REQUESTER_ID)
  void failToUpdateLikeByBadRequestWhenNoGatheringId() throws Exception {
    //given
    //when
    //then
    mockMvc.perform(put(BASE_URL + "?gatheringid=")
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockCustomUser(memberId = REQUESTER_ID)
  void failToUpdateLikeByNotFount() throws Exception {
    //given
    given(gatheringLikeService.doLike(anyLong(), anyLong()))
        .willThrow(new CustomException(ExceptionCode.ENTITY_NOT_FOUND));
    //when
    //then
    mockMvc.perform(put(BASE_URL + "?gatheringid=" + GATHERING_ID)
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isNotFound());
  }
}