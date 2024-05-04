package com.withus.withmebe.comment.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
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
  private final long gatheringId = 1L;
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
    mockMvc.perform(post(baseUrl + "/add?gatheringid=1")
            .contentType(MediaType.APPLICATION_JSON)
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

  @Test
  @WithMockUser
  void seccessToGetComments() throws Exception {
    //given
    CommentResponse commentResponse2 = CommentResponse.builder()
        .id(2L)
        .nickName("홍길동2")
        .commentContent("내용2")
        .createdDttm(LocalDateTime.of(2023, 4, 5, 6, 7, 8))
        .updatedDttm(LocalDateTime.of(2024, 5, 6, 7, 8, 9))
        .build();
    Pageable pageable = PageRequest.of(0, 10);

    given(commentService.readComments(anyLong(), any()))
        .willReturn(
            new PageImpl<CommentResponse>(List.of(commentResponse, commentResponse2), pageable, 2));

    //when
    //then
    mockMvc.perform(get(baseUrl + "/list/" + gatheringId)
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.totalElements").value(2))
        .andExpect(jsonPath("$.totalPages").value(1))
        .andExpect(jsonPath("$.pageable.pageNumber").value(0))
        .andExpect(jsonPath("$.content[0].id").value(commentResponse.id()))
        .andExpect(jsonPath("$.content[0].nickName").value(commentResponse.nickName()))
        .andExpect(jsonPath("$.content[0].commentContent").value(commentResponse.commentContent()))
        .andExpect(jsonPath("$.content[0].createdDttm").value(commentResponse.createdDttm().toString()))
        .andExpect(jsonPath("$.content[0].updatedDttm").value(commentResponse.updatedDttm().toString()))
        .andExpect(jsonPath("$.content[1].id").value(commentResponse2.id()))
        .andExpect(jsonPath("$.content[1].nickName").value(commentResponse2.nickName()))
        .andExpect(jsonPath("$.content[1].commentContent").value(commentResponse2.commentContent()))
        .andExpect(jsonPath("$.content[1].createdDttm").value(commentResponse2.createdDttm().toString()))
        .andExpect(jsonPath("$.content[1].updatedDttm").value(commentResponse2.updatedDttm().toString()));
  }

  @Test
  @WithMockUser
  void failToGetCommentsByBadRequest() throws Exception {
    //given
    //when
    //then
    mockMvc.perform(get(baseUrl + "/list")
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockCustomUser
  void seccessToSetComment() throws Exception {
    //given
    given(commentService.updateComment(anyLong(), anyLong(), any()))
        .willReturn(commentResponse);

    //when
    //then
    mockMvc.perform(put(baseUrl + "/" + commentResponse.id())
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
  void failToSetCommentByBadRequest() throws Exception {
    //given
    //when
    //then
    mockMvc.perform(put(baseUrl + "/" + commentResponse.id())
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockCustomUser
  void failToSetCommentByNotFound() throws Exception {
    //given
    given(commentService.updateComment(anyLong(), anyLong(), any()))
        .willThrow(new CustomException(ExceptionCode.ENTITY_NOT_FOUND));

    //when
    //then
    mockMvc.perform(put(baseUrl + "/" + commentResponse.id())
            .contentType(MediaType.APPLICATION_JSON)
            .content(gson.toJson(jsonObject))
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isNotFound());
  }

  @Test
  @WithMockCustomUser
  void failToSetCommentByAuthenticationIssue() throws Exception {
    //given
    given(commentService.updateComment(anyLong(), anyLong(), any()))
        .willThrow(new CustomException(ExceptionCode.AUTHENTICATION_ISSUE));
    //when
    //then
    mockMvc.perform(put(baseUrl + "/" + commentResponse.id())
            .contentType(MediaType.APPLICATION_JSON)
            .content(gson.toJson(jsonObject))
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockCustomUser
  void failToSetCommentByAuthorizationIssue() throws Exception {
    //given
    given(commentService.updateComment(anyLong(), anyLong(), any()))
        .willThrow(new CustomException(ExceptionCode.AUTHORIZATION_ISSUE));
    //when
    //then
    mockMvc.perform(put(baseUrl + "/" + commentResponse.id())
            .contentType(MediaType.APPLICATION_JSON)
            .content(gson.toJson(jsonObject))
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isForbidden());
  }

  @Test
  @WithMockCustomUser
  void seccessToDeleteComment() throws Exception {
    //given
    given(commentService.deleteComment(anyLong(), anyLong()))
        .willReturn(commentResponse);

    //when
    //then
    mockMvc.perform(delete(baseUrl + "/" + commentResponse.id())
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
  void failToDeleteCommentByBadRequest() throws Exception {
    //given
    //when
    //then
    mockMvc.perform(delete(baseUrl + "/adfad")
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockCustomUser
  void failToDeleteCommentByNotFound() throws Exception {
    //given
    given(commentService.deleteComment(anyLong(), anyLong()))
        .willThrow(new CustomException(ExceptionCode.ENTITY_NOT_FOUND));

    //when
    //then
    mockMvc.perform(delete(baseUrl + "/" + commentResponse.id())
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isNotFound());
  }

  @Test
  @WithMockCustomUser
  void failToDeleteCommentByAuthenticationIssue() throws Exception {
    //given
    given(commentService.deleteComment(anyLong(), anyLong()))
        .willThrow(new CustomException(ExceptionCode.AUTHENTICATION_ISSUE));
    //when
    //then
    mockMvc.perform(delete(baseUrl + "/" + commentResponse.id())
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isUnauthorized());
  }
  @Test
  @WithMockCustomUser
  void failToDeleteCommentByAuthorizationIssue() throws Exception {
    //given
    given(commentService.deleteComment(anyLong(), anyLong()))
        .willThrow(new CustomException(ExceptionCode.AUTHORIZATION_ISSUE));
    //when
    //then
    mockMvc.perform(delete(baseUrl + "/" + commentResponse.id())
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isForbidden());
  }
}