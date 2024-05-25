package com.withus.withmebe.comment.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
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
  private static final Gson GSON = new Gson();
  private static final long REQUESTER_ID = 1L;
  private static final long GATHERING_ID = 2L;
  private static final long COMMENT_ID = 3L;

  @Test
  @WithMockCustomUser(memberId = REQUESTER_ID)
  void successToAddComment() throws Exception {
    //given
    final JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("commentContent", "");
    final CommentResponse commentResponse =
        getStubbedComment(COMMENT_ID, GATHERING_ID, getStubbedMember(REQUESTER_ID)).toResponse();

    given(commentService.createComment(eq(REQUESTER_ID), eq(GATHERING_ID), any(AddCommentRequest.class)))
        .willReturn(commentResponse);

    //when
    //then
    mockMvc.perform(post(BASE_URL + "/add?gatheringid=" + GATHERING_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(GSON.toJson(jsonObject))
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(commentResponse.id()))
        .andExpect(jsonPath("$.nickName").value(commentResponse.nickName()))
        .andExpect(jsonPath("$.profileImg").value(commentResponse.profileImg()))
        .andExpect(jsonPath("$.commentContent").value(commentResponse.commentContent()))
        .andExpect(jsonPath("$.createdDttm")
            .value(commentResponse.createdDttm().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
        .andExpect(jsonPath("$.updatedDttm")
            .value(commentResponse.updatedDttm().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
  }

  @Test
  @WithMockCustomUser(memberId = REQUESTER_ID)
  void failToAddCommentByBadRequestNoGatheringId() throws Exception {
    //given
    final JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("commentContent", "");

    //when
    //then
    mockMvc.perform(post(BASE_URL + "/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(GSON.toJson(jsonObject))
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockCustomUser(memberId = REQUESTER_ID)
  void failToAddCommentByBadRequestWhenNoRequestBody() throws Exception {
    //given
    //when
    //then
    mockMvc.perform(post(BASE_URL + "/add?gatheringid=" + GATHERING_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockCustomUser(memberId = REQUESTER_ID)
  void failToAddCommentByBadRequestWhenCommentContentIsNull() throws Exception {
    //given
    final JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("commentContent", (String) null);

    //when
    //then
    mockMvc.perform(post(BASE_URL + "/add?gatheringid=" + GATHERING_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(GSON.toJson(jsonObject))
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockCustomUser(memberId = REQUESTER_ID)
  void failToAddCommentByNotFound() throws Exception {
    //given
    final JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("commentContent", "");

    given(commentService.createComment(anyLong(), anyLong(), any()))
        .willThrow(new CustomException(ExceptionCode.ENTITY_NOT_FOUND));

    //when
    //then
    mockMvc.perform(post(BASE_URL + "/add?gatheringid=" + GATHERING_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(GSON.toJson(jsonObject))
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isNotFound());
  }

  @Test
  @WithMockCustomUser(memberId = REQUESTER_ID)
  void failToAddCommentByAuthenticationIssue() throws Exception {
    //given
    final JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("commentContent", "");

    given(commentService.createComment(anyLong(), anyLong(), any()))
        .willThrow(new CustomException(ExceptionCode.AUTHENTICATION_ISSUE));

    //when
    //then
    mockMvc.perform(post(BASE_URL + "/add?gatheringid=" + GATHERING_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(GSON.toJson(jsonObject))
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser
  void successToGetComments() throws Exception {
    //given
    final CommentResponse commentResponse1 =
        getStubbedComment(COMMENT_ID, GATHERING_ID, getStubbedMember(REQUESTER_ID)).toResponse();
    final CommentResponse commentResponse2 =
        getStubbedComment(COMMENT_ID+1, GATHERING_ID, getStubbedMember(REQUESTER_ID+1)).toResponse();


    given(commentService.readComments(eq(GATHERING_ID), any(Pageable.class)))
        .willReturn(
            new PageImpl<CommentResponse>(List.of(commentResponse1, commentResponse2),
                Pageable.ofSize(10),
                2));

    //when
    //then
    mockMvc.perform(get(BASE_URL + "/list/" + GATHERING_ID)
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.totalElements").value(2))
        .andExpect(jsonPath("$.totalPages").value(1))
        .andExpect(jsonPath("$.pageable.pageNumber").value(0))
        .andExpect(jsonPath("$.content[0].id").value(commentResponse1.id()))
        .andExpect(jsonPath("$.content[0].nickName").value(commentResponse1.nickName()))
        .andExpect(jsonPath("$.content[0].profileImg").value(commentResponse1.profileImg()))
        .andExpect(jsonPath("$.content[0].commentContent").value(commentResponse1.commentContent()))
        .andExpect(jsonPath("$.content[0].createdDttm")
            .value(commentResponse1.createdDttm().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
        .andExpect(jsonPath("$.content[0].updatedDttm")
            .value(commentResponse1.updatedDttm().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
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
  void failToGetCommentsByBadRequestWhenNoGatheringId() throws Exception {
    //given
    //when
    //then
    mockMvc.perform(get(BASE_URL + "/list")
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockCustomUser(memberId = REQUESTER_ID)
  void successToSetComment() throws Exception {
    //given
    final JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("commentContent", "");
    final CommentResponse commentResponse =
        getStubbedComment(COMMENT_ID, GATHERING_ID, getStubbedMember(REQUESTER_ID)).toResponse();

    given(commentService.updateComment(eq(REQUESTER_ID), eq(COMMENT_ID), any(SetCommentRequest.class)))
        .willReturn(commentResponse);

    //when
    //then
    mockMvc.perform(put(BASE_URL + "/" + COMMENT_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(GSON.toJson(jsonObject))
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(commentResponse.id()))
        .andExpect(jsonPath("$.nickName").value(commentResponse.nickName()))
        .andExpect(jsonPath("$.profileImg").value(commentResponse.profileImg()))
        .andExpect(jsonPath("$.commentContent").value(commentResponse.commentContent()))
        .andExpect(jsonPath("$.createdDttm")
            .value(commentResponse.createdDttm().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
        .andExpect(jsonPath("$.updatedDttm")
            .value(commentResponse.updatedDttm().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
  }

  @Test
  @WithMockCustomUser(memberId = REQUESTER_ID)
  void failToSetCommentByBadRequestWhenNoRequestBody() throws Exception {
    //given
    //when
    //then
    mockMvc.perform(put(BASE_URL + "/" + COMMENT_ID)
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockCustomUser(memberId = REQUESTER_ID)
  void failToSetCommentByBadRequest() throws Exception {
    //given
    final JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("commentContent", "");

    //when
    //then
    mockMvc.perform(put(BASE_URL + "/size=10")
            .content(GSON.toJson(jsonObject))
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockCustomUser(memberId = REQUESTER_ID)
  void failToSetCommentByBadRequestWhenCommentContentIsNull() throws Exception {
    //given
    final JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("commentContent", (String) null);

    //when
    //then
    mockMvc.perform(put(BASE_URL + "/" + COMMENT_ID)
            .content(GSON.toJson(jsonObject))
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockCustomUser(memberId = REQUESTER_ID)
  void failToSetCommentByNotFound() throws Exception {
    //given
    final JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("commentContent", "");

    given(commentService.updateComment(anyLong(), anyLong(), any()))
        .willThrow(new CustomException(ExceptionCode.ENTITY_NOT_FOUND));

    //when
    //then
    mockMvc.perform(put(BASE_URL + "/" + COMMENT_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(GSON.toJson(jsonObject))
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isNotFound());
  }

  @Test
  @WithMockCustomUser(memberId = REQUESTER_ID)
  void failToSetCommentByAuthenticationIssue() throws Exception {
    //given
    final JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("commentContent", "");

    given(commentService.updateComment(anyLong(), anyLong(), any()))
        .willThrow(new CustomException(ExceptionCode.AUTHENTICATION_ISSUE));

    //when
    //then
    mockMvc.perform(put(BASE_URL + "/" + COMMENT_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(GSON.toJson(jsonObject))
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockCustomUser(memberId = REQUESTER_ID)
  void failToSetCommentByAuthorizationIssue() throws Exception {
    //given
    final JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("commentContent", "");

    given(commentService.updateComment(anyLong(), anyLong(), any()))
        .willThrow(new CustomException(ExceptionCode.AUTHORIZATION_ISSUE));

    //when
    //then
    mockMvc.perform(put(BASE_URL + "/" + COMMENT_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(GSON.toJson(jsonObject))
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isForbidden());
  }

  @Test
  @WithMockCustomUser(memberId = REQUESTER_ID)
  void successToDeleteComment() throws Exception {
    //given
    final CommentResponse commentResponse =
        getStubbedComment(COMMENT_ID, GATHERING_ID, getStubbedMember(REQUESTER_ID)).toResponse();

    given(commentService.deleteComment(anyLong(), anyLong()))
        .willReturn(commentResponse);

    //when
    //then
    mockMvc.perform(delete(BASE_URL + "/" + COMMENT_ID)
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(commentResponse.id()))
        .andExpect(jsonPath("$.nickName").value(commentResponse.nickName()))
        .andExpect(jsonPath("$.profileImg").value(commentResponse.profileImg()))
        .andExpect(jsonPath("$.commentContent").value(commentResponse.commentContent()))
        .andExpect(jsonPath("$.createdDttm")
            .value(commentResponse.createdDttm().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
        .andExpect(jsonPath("$.updatedDttm")
            .value(commentResponse.updatedDttm().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
  }

  @Test
  @WithMockCustomUser(memberId = REQUESTER_ID)
  void failToDeleteCommentByBadRequest() throws Exception {
    //given
    //when
    //then
    mockMvc.perform(delete(BASE_URL + "/size=10")
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockCustomUser(memberId = REQUESTER_ID)
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
  @WithMockCustomUser(memberId = REQUESTER_ID)
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
  @WithMockCustomUser(memberId = REQUESTER_ID)
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