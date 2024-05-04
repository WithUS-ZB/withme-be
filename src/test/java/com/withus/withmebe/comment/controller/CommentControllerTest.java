package com.withus.withmebe.comment.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.withus.withmebe.comment.dto.response.CommentResponse;
import com.withus.withmebe.comment.service.CommentService;
import com.withus.withmebe.common.exception.CustomException;
import com.withus.withmebe.common.exception.ExceptionCode;
import com.withus.withmebe.security.jwt.filter.JwtAuthenticationFilter;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeAll;
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
import util.WithMockCustomUser;


@WebMvcTest(controllers = CommentController.class, excludeFilters = {
    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)})
@MockBean(JpaMetamodelMappingContext.class)
class CommentControllerTest {

  @MockBean
  private CommentService commentService;

  @Autowired
  private MockMvc mockMvc;

  private final String baseUrl = "/api/comment";
  private final Gson gson = new Gson();
  private static final JsonObject jsonObject = new JsonObject();

  private final CommentResponse commentResponse = CommentResponse.builder()
      .id(1L)
      .nickName("홍길동")
      .commentContent("내용")
      .createdDttm(LocalDateTime.of(2021, 2, 3, 4, 5, 6))
      .updatedDttm(LocalDateTime.of(2022, 3, 4, 5, 6, 7))
      .build();

  @BeforeAll
  static void setUp() {
    jsonObject.addProperty("commentContent", "아무내용");
  }

  @Test
  @WithMockCustomUser
  void seccessToAddComment() throws Exception {
    //given
    given(commentService.createComment(anyLong(), anyLong(), any()))
        .willReturn(commentResponse);

    //when
    //then
    mockMvc.perform(post(baseUrl + "/add?gatheringid=1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(gson.toJson(jsonObject))
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(commentResponse.id()))
        .andExpect(jsonPath("$.nickName").value(commentResponse.nickName()))
        .andExpect(jsonPath("$.commentContent").value(commentResponse.commentContent()))
        .andExpect(jsonPath("$.createdDttm").value(commentResponse.createdDttm().toString()))
        .andExpect(jsonPath("$.updatedDttm").value(commentResponse.updatedDttm().toString()));
  }

  @Test
  @WithMockCustomUser
  void failToAddCommentByBadRequest() throws Exception {
    //given
    //when
    //then
    mockMvc.perform(post(baseUrl + "/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(gson.toJson(jsonObject))
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockCustomUser
  void failToAddCommentByNotFound() throws Exception {
    //given
    given(commentService.createComment(anyLong(), anyLong(), any()))
        .willThrow(new CustomException(ExceptionCode.ENTITY_NOT_FOUND));

    //when
    //then
    mockMvc.perform(post(baseUrl + "/add?gatheringid=1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(gson.toJson(jsonObject))
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isNotFound());
  }

  @Test
  @WithMockCustomUser
  void failToAddCommentByAuthenticationIssue() throws Exception {
    //given
    given(commentService.createComment(anyLong(), anyLong(), any()))
        .willThrow(new CustomException(ExceptionCode.AUTHENTICATION_ISSUE));

    //when
    //then
    mockMvc.perform(post(baseUrl + "/add?gatheringid=1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(gson.toJson(jsonObject))
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isUnauthorized());
  }

}