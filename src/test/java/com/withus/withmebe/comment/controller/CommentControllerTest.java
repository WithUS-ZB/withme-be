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
import static util.objectprovider.CommentProvider.getStubbedComment;
import static util.objectprovider.MemberProvider.getStubbedMember;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.withus.withmebe.comment.dto.request.AddCommentRequest;
import com.withus.withmebe.comment.dto.request.SetCommentRequest;
import com.withus.withmebe.comment.dto.response.CommentResponse;
import com.withus.withmebe.comment.service.CommentService;
import com.withus.withmebe.common.exception.CustomException;
import com.withus.withmebe.common.exception.ExceptionCode;
import com.withus.withmebe.security.jwt.filter.JwtAuthenticationFilter;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import util.security.WithMockCustomUser;


@WebMvcTest(controllers = CommentController.class, excludeFilters = {
    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)})
@MockBean(JpaMetamodelMappingContext.class)
class CommentControllerTest {

  @MockBean
  private CommentService commentService;

  @Autowired
  private MockMvc mockMvc;

  private static final String BASE_URL = "/api/comment";
  private static final long GATHERING_ID = 1L;
  private static final long COMMENT_ID = 2L;
  private static final Gson GSON = new Gson();
  private static final JsonObject JSON_OBJECT = new JsonObject();
  private static final Pageable PAGEABLE = Pageable.ofSize(10);

  private final CommentResponse COMMENT_RESPONSE =
      getStubbedComment(COMMENT_ID, GATHERING_ID, getStubbedMember(3L)).toResponse();

  @BeforeAll
  static void setUp() {
    JSON_OBJECT.addProperty("commentContent", "아무내용");
  }

  @Test
  @WithMockCustomUser
  void seccessToAddComment() throws Exception {
    //given
    given(commentService.createComment(anyLong(), anyLong(), any(AddCommentRequest.class)))
        .willReturn(COMMENT_RESPONSE);

    //when
    //then
    mockMvc.perform(post(BASE_URL + "/add?gatheringid=" + GATHERING_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(GSON.toJson(JSON_OBJECT))
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(COMMENT_RESPONSE.id()))
        .andExpect(jsonPath("$.nickName").value(COMMENT_RESPONSE.nickName()))
        .andExpect(jsonPath("$.profileImg").value(COMMENT_RESPONSE.profileImg()))
        .andExpect(jsonPath("$.commentContent").value(COMMENT_RESPONSE.commentContent()))
        .andExpect(jsonPath("$.createdDttm")
            .value(COMMENT_RESPONSE.createdDttm().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
        .andExpect(jsonPath("$.updatedDttm")
            .value(COMMENT_RESPONSE.updatedDttm().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
  }

  @Test
  @WithMockCustomUser
  void failToAddCommentByBadRequest() throws Exception {
    //given
    //when
    //then
    mockMvc.perform(post(BASE_URL + "/add?gatheringid=" + GATHERING_ID)
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
    mockMvc.perform(post(BASE_URL + "/add?gatheringid=" + GATHERING_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(GSON.toJson(JSON_OBJECT))
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isNotFound());
  }

  @Test
  @WithMockCustomUser
  void failToAddCommentByAuthenticationIssue() throws Exception {
    //given
    given(commentService.createComment(anyLong(), anyLong(), any(AddCommentRequest.class)))
        .willThrow(new CustomException(ExceptionCode.AUTHENTICATION_ISSUE));

    //when
    //then
    mockMvc.perform(post(BASE_URL + "/add?gatheringid=" + GATHERING_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(GSON.toJson(JSON_OBJECT))
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser
  void seccessToGetComments() throws Exception {
    //given
    CommentResponse commentResponse2 =
        getStubbedComment(3L, GATHERING_ID, getStubbedMember(4L)).toResponse();

    given(commentService.readComments(anyLong(), any(Pageable.class)))
        .willReturn(
            new PageImpl<CommentResponse>(List.of(COMMENT_RESPONSE, commentResponse2), PAGEABLE,
                2));

    //when
    //then
    mockMvc.perform(get(BASE_URL + "/list/" + GATHERING_ID)
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.totalElements").value(2))
        .andExpect(jsonPath("$.totalPages").value(1))
        .andExpect(jsonPath("$.pageable.pageNumber").value(0))
        .andExpect(jsonPath("$.content[0].id").value(COMMENT_RESPONSE.id()))
        .andExpect(jsonPath("$.content[0].nickName").value(COMMENT_RESPONSE.nickName()))
        .andExpect(jsonPath("$.content[0].profileImg").value(COMMENT_RESPONSE.profileImg()))
        .andExpect(jsonPath("$.content[0].commentContent").value(COMMENT_RESPONSE.commentContent()))
        .andExpect(jsonPath("$.content[0].createdDttm")
            .value(COMMENT_RESPONSE.createdDttm().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
        .andExpect(jsonPath("$.content[0].updatedDttm")
            .value(COMMENT_RESPONSE.updatedDttm().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
        .andExpect(jsonPath("$.content[1].id").value(commentResponse2.id()))
        .andExpect(jsonPath("$.content[1].nickName").value(commentResponse2.nickName()))
        .andExpect(jsonPath("$.content[1].profileImg").value(commentResponse2.profileImg()))
        .andExpect(jsonPath("$.content[1].commentContent").value(commentResponse2.commentContent()))
        .andExpect(jsonPath("$.content[1].createdDttm")
            .value(commentResponse2.createdDttm().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
        .andExpect(jsonPath("$.content[1].updatedDttm")
            .value(commentResponse2.updatedDttm().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
  }

  @Test
  @WithMockUser
  void failToGetCommentsByBadRequest() throws Exception {
    //given
    //when
    //then
    mockMvc.perform(get(BASE_URL + "/list")
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockCustomUser
  void successToSetComment() throws Exception {
    //given
    given(commentService.updateComment(anyLong(), anyLong(), any(SetCommentRequest.class)))
        .willReturn(COMMENT_RESPONSE);

    //when
    //then
    mockMvc.perform(put(BASE_URL + "/" + COMMENT_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(GSON.toJson(JSON_OBJECT))
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(COMMENT_RESPONSE.id()))
        .andExpect(jsonPath("$.nickName").value(COMMENT_RESPONSE.nickName()))
        .andExpect(jsonPath("$.profileImg").value(COMMENT_RESPONSE.profileImg()))
        .andExpect(jsonPath("$.commentContent").value(COMMENT_RESPONSE.commentContent()))
        .andExpect(jsonPath("$.createdDttm")
            .value(COMMENT_RESPONSE.createdDttm().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
        .andExpect(jsonPath("$.updatedDttm")
            .value(COMMENT_RESPONSE.updatedDttm().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
  }

  @Test
  @WithMockCustomUser
  void failToSetCommentByBadRequest() throws Exception {
    //given
    //when
    //then
    mockMvc.perform(put(BASE_URL + "/" + COMMENT_ID)
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
    mockMvc.perform(put(BASE_URL + "/" + COMMENT_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(GSON.toJson(JSON_OBJECT))
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
    mockMvc.perform(put(BASE_URL + "/" + COMMENT_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(GSON.toJson(JSON_OBJECT))
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
    mockMvc.perform(put(BASE_URL + "/" + COMMENT_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(GSON.toJson(JSON_OBJECT))
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isForbidden());
  }

  @Test
  @WithMockCustomUser
  void seccessToDeleteComment() throws Exception {
    //given
    given(commentService.deleteComment(anyLong(), anyLong()))
        .willReturn(COMMENT_RESPONSE);

    //when
    //then
    mockMvc.perform(delete(BASE_URL + "/" + COMMENT_ID)
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(COMMENT_RESPONSE.id()))
        .andExpect(jsonPath("$.nickName").value(COMMENT_RESPONSE.nickName()))
        .andExpect(jsonPath("$.profileImg").value(COMMENT_RESPONSE.profileImg()))
        .andExpect(jsonPath("$.commentContent").value(COMMENT_RESPONSE.commentContent()))
        .andExpect(jsonPath("$.createdDttm")
            .value(COMMENT_RESPONSE.createdDttm().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
        .andExpect(jsonPath("$.updatedDttm")
            .value(COMMENT_RESPONSE.updatedDttm().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
  }

  @Test
  @WithMockCustomUser
  void failToDeleteCommentByBadRequest() throws Exception {
    //given
    //when
    //then
    mockMvc.perform(delete(BASE_URL + "/size=1")
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
    mockMvc.perform(delete(BASE_URL + "/" + COMMENT_ID)
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
    mockMvc.perform(delete(BASE_URL + "/" + COMMENT_ID)
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
    mockMvc.perform(delete(BASE_URL + "/" + COMMENT_ID)
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isForbidden());
  }
}