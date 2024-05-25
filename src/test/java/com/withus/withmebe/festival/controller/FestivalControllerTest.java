package com.withus.withmebe.festival.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static util.objectprovider.FestivalProvider.getStubbedFestival;

import com.withus.withmebe.festival.dto.FestivalSimpleInfo;
import com.withus.withmebe.festival.service.FestivalService;
import com.withus.withmebe.security.jwt.filter.JwtAuthenticationFilter;
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

@WebMvcTest(controllers = FestivalController.class, excludeFilters = {
    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)})
@MockBean(JpaMetamodelMappingContext.class)
class FestivalControllerTest {

  @MockBean
  private FestivalService festivalService;

  @Autowired
  private MockMvc mockMvc;

  private static final String BASE_URL = "/api/festival";

  @Test
  @WithMockUser
  void successToGetFestivals() throws Exception {
    //given
    FestivalSimpleInfo festivalSimpleInfo1 = getStubbedFestival(1L).toFestivalSimpleInfo();
    FestivalSimpleInfo festivalSimpleInfo2 = getStubbedFestival(2L).toFestivalSimpleInfo();

    given(festivalService.readFestivals(any(Pageable.class)))
        .willReturn(
            new PageImpl<FestivalSimpleInfo>(List.of(festivalSimpleInfo1, festivalSimpleInfo2),
                Pageable.ofSize(6), 2));

    //when
    //then
    mockMvc.perform(get(BASE_URL)
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.totalElements").value(2))
        .andExpect(jsonPath("$.totalPages").value(1))
        .andExpect(jsonPath("$.pageable.pageNumber").value(0))
        .andExpect(jsonPath("$.content[0].title").value(festivalSimpleInfo1.title()))
        .andExpect(jsonPath("$.content[0].img").value(festivalSimpleInfo1.img()))
        .andExpect(jsonPath("$.content[0].link").value(festivalSimpleInfo1.link()))
        .andExpect(jsonPath("$.content[1].title").value(festivalSimpleInfo2.title()))
        .andExpect(jsonPath("$.content[1].img").value(festivalSimpleInfo2.img()))
        .andExpect(jsonPath("$.content[1].link").value(festivalSimpleInfo2.link()));
  }
}