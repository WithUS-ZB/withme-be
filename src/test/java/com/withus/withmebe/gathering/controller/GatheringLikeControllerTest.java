package com.withus.withmebe.gathering.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.withus.withmebe.gathering.service.GatheringLikeService;
import com.withus.withmebe.security.jwt.filter.JwtAuthenticationFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
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

  @Test
  @WithMockCustomUser
  void successToUpdateLike() throws Exception {
    //given
    given(gatheringLikeService.doLike(anyLong(), anyLong()))
        .willReturn(true);
    //when
    //then
    mockMvc.perform(put(BASE_URL + "?gatheringid=1")
            .contentType(MediaType.APPLICATION_JSON)
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isOk())
        .andExpect(content().string("true"));
  }

  @Test
  @WithMockCustomUser
  void failToUpdateLikeByBadRequest() throws Exception {
    //given
    //when
    //then
    mockMvc.perform(put(BASE_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isBadRequest());
  }
}