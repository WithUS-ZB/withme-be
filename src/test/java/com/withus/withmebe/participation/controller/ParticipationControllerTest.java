package com.withus.withmebe.participation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static util.objectprovider.GatheringProvider.getStubbedGathering;
import static util.objectprovider.MemberProvider.getStubbedMember;
import static util.objectprovider.ParticipationProvider.getStubbedParticipation;
import static util.objectprovider.ParticipationProvider.getStubbedParticipationByStatus;

import com.withus.withmebe.common.exception.CustomException;
import com.withus.withmebe.common.exception.ExceptionCode;
import com.withus.withmebe.gathering.entity.Gathering;
import com.withus.withmebe.participation.dto.GatheringParticipationSimpleInfo;
import com.withus.withmebe.participation.dto.MyParticipationSimpleInfo;
import com.withus.withmebe.participation.dto.ParticipationResponse;
import com.withus.withmebe.participation.entity.Participation;
import com.withus.withmebe.participation.service.ParticipationService;
import com.withus.withmebe.participation.type.Status;
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

@WebMvcTest(controllers = ParticipationController.class, excludeFilters = {
    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)})
@MockBean(JpaMetamodelMappingContext.class)
class ParticipationControllerTest {

  @MockBean
  private ParticipationService participationService;

  @Autowired
  private MockMvc mockMvc;

  private static final String BASE_URL = "/api/participation";
  private static final long PARTICIPATION_ID = 1L;
  private static final long PARTICIPANT_ID = 2L;
  private static final long GATHERING_ID = 3L;
  private static final long HOST_ID = 4L;
  private static final Gathering STUBBED_GATHERING = getStubbedGathering(GATHERING_ID, HOST_ID);
  private static final Pageable PAGEABLE = Pageable.ofSize(10);
  private static final Participation STUBBED_PARTICIPATION =
      getStubbedParticipation(PARTICIPATION_ID, getStubbedMember(PARTICIPANT_ID),
          STUBBED_GATHERING);
  private static final ParticipationResponse PARTICIPATION_RESPONSE = STUBBED_PARTICIPATION.toResponse();

  @Test
  @WithMockCustomUser
  void successToAddParticipation() throws Exception {
    //given
    given(participationService.createParticipation(anyLong(), anyLong()))
        .willReturn(PARTICIPATION_RESPONSE);

    //when
    //then
    mockMvc.perform(post(BASE_URL + "?gatheringid=" + GATHERING_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(PARTICIPATION_RESPONSE.id()))
        .andExpect(jsonPath("$.nickName").value(PARTICIPATION_RESPONSE.nickName()))
        .andExpect(jsonPath("$.title").value(PARTICIPATION_RESPONSE.title()))
        .andExpect(jsonPath("$.status").value(PARTICIPATION_RESPONSE.status().toString()))
        .andExpect(jsonPath("$.createdDttm")
            .value(
                PARTICIPATION_RESPONSE.createdDttm().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
        .andExpect(jsonPath("$.updatedDttm")
            .value(PARTICIPATION_RESPONSE.updatedDttm()
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
  }

  @Test
  @WithMockCustomUser
  void failToAddParticipationByBadRequest() throws Exception {
    //given
    //when
    //then
    mockMvc.perform(post(BASE_URL + "?gatheringid")
            .contentType(MediaType.APPLICATION_JSON)
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockCustomUser
  void failToAddParticipationByNotFound() throws Exception {
    //given
    given(participationService.createParticipation(anyLong(), anyLong()))
        .willThrow(new CustomException(ExceptionCode.ENTITY_NOT_FOUND));

    //when
    //then
    mockMvc.perform(post(BASE_URL + "?gatheringid=" + GATHERING_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isNotFound());
  }

  @Test
  @WithMockCustomUser(isMobileAuthenticatedMember = false)
  void failToAddParticipationByNotMobileAuth() throws Exception {
    //given

    //when
    //then
    mockMvc.perform(post(BASE_URL + "?gatheringid=" + GATHERING_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockCustomUser
  void failToAddParticipationByParticipantsTypeConflict() throws Exception {
    //given
    given(participationService.createParticipation(anyLong(), anyLong()))
        .willThrow(new CustomException(ExceptionCode.PARTICIPANTSTYPE_CONFLICT));

    //when
    //then
    mockMvc.perform(post(BASE_URL + "?gatheringid=" + GATHERING_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isConflict());
  }

  @Test
  @WithMockCustomUser
  void failToAddParticipationByAuthorizationIssue() throws Exception {
    //given
    given(participationService.createParticipation(anyLong(), anyLong()))
        .willThrow(new CustomException(ExceptionCode.AUTHORIZATION_ISSUE));

    //when
    //then
    mockMvc.perform(post(BASE_URL + "?gatheringid=" + GATHERING_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isForbidden());
  }

  @Test
  @WithMockCustomUser
  void failToAddParticipationByParticipantsTypeGatheringIsCanceled() throws Exception {
    //given
    given(participationService.createParticipation(anyLong(), anyLong()))
        .willThrow(new CustomException(ExceptionCode.GATHERING_STATUS_CONFLICT));

    //when
    //then
    mockMvc.perform(post(BASE_URL + "?gatheringid=" + GATHERING_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isConflict());
  }

  @Test
  @WithMockCustomUser
  void failToAddParticipationByParticipationDuplicated() throws Exception {
    //given
    given(participationService.createParticipation(anyLong(), anyLong()))
        .willThrow(new CustomException(ExceptionCode.PARTICIPATION_DUPLICATED));

    //when
    //then
    mockMvc.perform(post(BASE_URL + "?gatheringid=" + GATHERING_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isConflict());
  }

  @Test
  @WithMockCustomUser
  void failToAddParticipationByParticipationPeriod() throws Exception {
    //given
    given(participationService.createParticipation(anyLong(), anyLong()))
        .willThrow(new CustomException(ExceptionCode.NOT_PARTICIPATION_PERIOD));

    //when
    //then
    mockMvc.perform(post(BASE_URL + "?gatheringid=" + GATHERING_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isConflict());
  }

  @Test
  @WithMockCustomUser
  void failToAddParticipationByParticipantCount() throws Exception {
    //given
    given(participationService.createParticipation(anyLong(), anyLong()))
        .willThrow(new CustomException(ExceptionCode.REACHED_AT_MAXIMUM_PARTICIPANT));

    //when
    //then
    mockMvc.perform(post(BASE_URL + "?gatheringid=" + GATHERING_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isConflict());
  }

  @Test
  @WithMockUser
  void successToGetApprovedParticipationCount() throws Exception {
    //given
    given(participationService.readApprovedParticipationCount(anyLong()))
        .willReturn(5L);

    //when
    //then
    mockMvc.perform(get(BASE_URL + "/count?gatheringid=" + GATHERING_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isOk())
        .andExpect(content().json("5"));
  }

  @Test
  @WithMockUser
  void failToGetApprovedParticipationCountByBadRequest() throws Exception {
    //given
    //when
    //then
    mockMvc.perform(get(BASE_URL + "/count?gatheringId=" + GATHERING_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockCustomUser
  void successToGetParticipations() throws Exception {
    //given
    GatheringParticipationSimpleInfo gatheringParticipationSimpleInfo1 =
        STUBBED_PARTICIPATION.toGatheringParticipationSimpleInfo();
    GatheringParticipationSimpleInfo gatheringParticipationSimpleInfo2 =
        getStubbedParticipationByStatus(PARTICIPATION_ID + 1,
            getStubbedMember(PARTICIPANT_ID + 1), STUBBED_GATHERING,
            Status.APPROVED).toGatheringParticipationSimpleInfo();

    given(participationService.readParticipations(anyLong(), anyLong(), any(Pageable.class)))
        .willReturn(new PageImpl<GatheringParticipationSimpleInfo>(
            List.of(gatheringParticipationSimpleInfo1, gatheringParticipationSimpleInfo2), PAGEABLE,
            2));

    //when
    //then

    mockMvc.perform(get(BASE_URL + "/list?gatheringid=" + GATHERING_ID)
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.totalElements").value(2))
        .andExpect(jsonPath("$.totalPages").value(1))
        .andExpect(jsonPath("$.pageable.pageNumber").value(0))
        .andExpect(jsonPath("$.content[0].id").value(gatheringParticipationSimpleInfo1.id()))
        .andExpect(
            jsonPath("$.content[0].nickName").value(gatheringParticipationSimpleInfo1.nickName()))
        .andExpect(jsonPath("$.content[0].status").value(
            gatheringParticipationSimpleInfo1.status().toString()))
        .andExpect(jsonPath("$.content[0].updatedDttm")
            .value(gatheringParticipationSimpleInfo1.updatedDttm()
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
        .andExpect(jsonPath("$.content[1].id").value(gatheringParticipationSimpleInfo2.id()))
        .andExpect(
            jsonPath("$.content[1].nickName").value(gatheringParticipationSimpleInfo2.nickName()))
        .andExpect(jsonPath("$.content[1].status").value(
            gatheringParticipationSimpleInfo2.status().toString()))
        .andExpect(jsonPath("$.content[1].updatedDttm")
            .value(gatheringParticipationSimpleInfo2.updatedDttm()
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
  }

  @Test
  @WithMockCustomUser
  void failToGetParticipationsByAUTHORIZATION_ISSUE() throws Exception {
    //given
    given(participationService.readParticipations(anyLong(), anyLong(), any(Pageable.class)))
        .willThrow(new CustomException(ExceptionCode.AUTHORIZATION_ISSUE));

    //when
    //then

    mockMvc.perform(get(BASE_URL + "/list?gatheringid=" + GATHERING_ID)
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isForbidden());
  }

  @Test
  @WithMockCustomUser
  void successToCancelParticipation() throws Exception {
    //given
    STUBBED_PARTICIPATION.setStatus(Status.CANCELED);
    ParticipationResponse canceledParticipationResponse =
        STUBBED_PARTICIPATION.toResponse();
    given(participationService.cancelParticipation(anyLong(), anyLong()))
        .willReturn(canceledParticipationResponse);

    //when
    //then

    mockMvc.perform(put(BASE_URL + "/cancel/" + PARTICIPATION_ID)
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(canceledParticipationResponse.id()))
        .andExpect(jsonPath("$.nickName").value(canceledParticipationResponse.nickName()))
        .andExpect(jsonPath("$.title").value(canceledParticipationResponse.title()))
        .andExpect(jsonPath("$.status").value(canceledParticipationResponse.status().toString()))
        .andExpect(jsonPath("$.createdDttm")
            .value(canceledParticipationResponse.createdDttm()
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
        .andExpect(jsonPath("$.updatedDttm")
            .value(canceledParticipationResponse.updatedDttm()
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
  }

  @Test
  @WithMockCustomUser
  void failToCancelParticipationByBadRequest() throws Exception {
    //given
    //when
    //then

    mockMvc.perform(put(BASE_URL + "/cancel/" + "PARTICIPATION_ID")
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockCustomUser
  void failToCancelParticipationByNotFound() throws Exception {
    //given
    given(participationService.cancelParticipation(anyLong(), anyLong()))
        .willThrow(new CustomException(ExceptionCode.ENTITY_NOT_FOUND));

    //when
    //then

    mockMvc.perform(put(BASE_URL + "/cancel/" + PARTICIPATION_ID)
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isNotFound());
  }

  @Test
  @WithMockCustomUser
  void failToCancelParticipationByAuthorizationIssue() throws Exception {
    //given
    given(participationService.cancelParticipation(anyLong(), anyLong()))
        .willThrow(new CustomException(ExceptionCode.AUTHORIZATION_ISSUE));

    //when
    //then

    mockMvc.perform(put(BASE_URL + "/cancel/" + PARTICIPATION_ID)
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isForbidden());
  }

  @Test
  @WithMockCustomUser
  void failToCancelParticipationByParticipationConflict() throws Exception {
    //given
    given(participationService.cancelParticipation(anyLong(), anyLong()))
        .willThrow(new CustomException(ExceptionCode.PARTICIPATION_CONFLICT));

    //when
    //then

    mockMvc.perform(put(BASE_URL + "/cancel/" + PARTICIPATION_ID)
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isConflict());
  }

  @Test
  @WithMockCustomUser
  void successToApproveParticipation() throws Exception {
    //given
    STUBBED_PARTICIPATION.setStatus(Status.APPROVED);
    ParticipationResponse approevdParticipationResponse =
        STUBBED_PARTICIPATION.toResponse();
    given(participationService.updateParticipationStatus(anyLong(), anyLong(),
        argThat(status -> status.equals(Status.APPROVED))))
        .willReturn(approevdParticipationResponse);

    //when
    //then

    mockMvc.perform(put(BASE_URL + "/approve/" + PARTICIPATION_ID)
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(approevdParticipationResponse.id()))
        .andExpect(jsonPath("$.nickName").value(approevdParticipationResponse.nickName()))
        .andExpect(jsonPath("$.title").value(approevdParticipationResponse.title()))
        .andExpect(jsonPath("$.status").value(approevdParticipationResponse.status().toString()))
        .andExpect(jsonPath("$.createdDttm")
            .value(approevdParticipationResponse.createdDttm()
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
        .andExpect(jsonPath("$.updatedDttm")
            .value(approevdParticipationResponse.updatedDttm()
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
  }

  @Test
  @WithMockCustomUser
  void failToApproveParticipationByBadRequest() throws Exception {
    //given
    //when
    //then

    mockMvc.perform(put(BASE_URL + "/approve/" + "PARTICIPATION_ID")
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockCustomUser
  void failToApproveParticipationByNotFound() throws Exception {
    //given
    given(participationService.updateParticipationStatus(anyLong(), anyLong(),
        argThat(status -> status.equals(Status.APPROVED))))
        .willThrow(new CustomException(ExceptionCode.ENTITY_NOT_FOUND));

    //when
    //then

    mockMvc.perform(put(BASE_URL + "/approve/" + PARTICIPATION_ID)
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isNotFound());
  }

  @Test
  @WithMockCustomUser
  void failToApproveParticipationByAuthorizationIssue() throws Exception {
    //given
    given(participationService.updateParticipationStatus(anyLong(), anyLong(),
        argThat(status -> status.equals(Status.APPROVED))))
        .willThrow(new CustomException(ExceptionCode.AUTHORIZATION_ISSUE));

    //when
    //then

    mockMvc.perform(put(BASE_URL + "/approve/" + PARTICIPATION_ID)
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isForbidden());
  }

  @Test
  @WithMockCustomUser
  void failToApproveParticipationByParticipationConflict() throws Exception {
    //given
    given(participationService.updateParticipationStatus(anyLong(), anyLong(),
        argThat(status -> status.equals(Status.APPROVED))))
        .willThrow(new CustomException(ExceptionCode.PARTICIPATION_CONFLICT));

    //when
    //then

    mockMvc.perform(put(BASE_URL + "/approve/" + PARTICIPATION_ID)
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isConflict());
  }

  @Test
  @WithMockCustomUser
  void failToApproveParticipationByReachedAtMaximumParticipant() throws Exception {
    //given
    given(participationService.updateParticipationStatus(anyLong(), anyLong(),
        argThat(status -> status.equals(Status.APPROVED))))
        .willThrow(new CustomException(ExceptionCode.REACHED_AT_MAXIMUM_PARTICIPANT));

    //when
    //then

    mockMvc.perform(put(BASE_URL + "/approve/" + PARTICIPATION_ID)
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isConflict());
  }

  @Test
  @WithMockCustomUser
  void successToRejectParticipation() throws Exception {
    //given
    STUBBED_PARTICIPATION.setStatus(Status.REJECTED);
    ParticipationResponse rejectedParticipationResponse =
        STUBBED_PARTICIPATION.toResponse();
    given(participationService.updateParticipationStatus(anyLong(), anyLong(),
        argThat(status -> status.equals(Status.REJECTED))))
        .willReturn(rejectedParticipationResponse);

    //when
    //then

    mockMvc.perform(put(BASE_URL + "/reject/" + PARTICIPATION_ID)
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(rejectedParticipationResponse.id()))
        .andExpect(jsonPath("$.nickName").value(rejectedParticipationResponse.nickName()))
        .andExpect(jsonPath("$.title").value(rejectedParticipationResponse.title()))
        .andExpect(jsonPath("$.status").value(rejectedParticipationResponse.status().toString()))
        .andExpect(jsonPath("$.createdDttm")
            .value(rejectedParticipationResponse.createdDttm()
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
        .andExpect(jsonPath("$.updatedDttm")
            .value(rejectedParticipationResponse.updatedDttm()
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
  }

  @Test
  @WithMockCustomUser
  void failToRejectParticipationByBadRequest() throws Exception {
    //given
    //when
    //then

    mockMvc.perform(put(BASE_URL + "/reject/" + "PARTICIPATION_ID")
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockCustomUser
  void failToRejectParticipationByNotFound() throws Exception {
    //given
    given(participationService.updateParticipationStatus(anyLong(), anyLong(),
        argThat(status -> status.equals(Status.REJECTED))))
        .willThrow(new CustomException(ExceptionCode.ENTITY_NOT_FOUND));

    //when
    //then

    mockMvc.perform(put(BASE_URL + "/reject/" + PARTICIPATION_ID)
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isNotFound());
  }

  @Test
  @WithMockCustomUser
  void failToRejectParticipationByAuthorizationIssue() throws Exception {
    //given
    given(participationService.updateParticipationStatus(anyLong(), anyLong(),
        argThat(status -> status.equals(Status.REJECTED))))
        .willThrow(new CustomException(ExceptionCode.AUTHORIZATION_ISSUE));

    //when
    //then

    mockMvc.perform(put(BASE_URL + "/reject/" + PARTICIPATION_ID)
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isForbidden());
  }

  @Test
  @WithMockCustomUser
  void failToRejectParticipationByParticipationConflict() throws Exception {
    //given
    given(participationService.updateParticipationStatus(anyLong(), anyLong(),
        argThat(status -> status.equals(Status.REJECTED))))
        .willThrow(new CustomException(ExceptionCode.PARTICIPATION_CONFLICT));

    //when
    //then

    mockMvc.perform(put(BASE_URL + "/reject/" + PARTICIPATION_ID)
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isConflict());
  }

  @Test
  @WithMockCustomUser
  void successToIsParticipatedWhenTrue() throws Exception {
    //given
    given(participationService.isParticipated(anyLong(), anyLong()))
        .willReturn(true);

    //when
    //then
    mockMvc.perform(get(BASE_URL + "?gatheringid=" + PARTICIPATION_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isOk())
        .andExpect(content().string("true"));
  }

  @Test
  @WithMockCustomUser
  void successToIsParticipatedWhenFalse() throws Exception {
    //given
    given(participationService.isParticipated(anyLong(), anyLong()))
        .willReturn(false);

    //when
    //then
    mockMvc.perform(get(BASE_URL + "?gatheringid=" + PARTICIPATION_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isOk())
        .andExpect(content().string("false"));
  }

  @Test
  @WithMockCustomUser
  void failToGetMyParticipationByEntityNotFound() throws Exception {
    //given
    given(participationService.isParticipated(anyLong(), anyLong()))
        .willThrow(new CustomException(ExceptionCode.ENTITY_NOT_FOUND));

    //when
    //then
    mockMvc.perform(get(BASE_URL + "/" + PARTICIPATION_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isNotFound());
  }

  @Test
  @WithMockCustomUser
  void successToGetMyParticipations() throws Exception {
    //given
    MyParticipationSimpleInfo myParticipationSimpleInfo1 =
        STUBBED_PARTICIPATION.toMyParticipationSimpleInfo();
    MyParticipationSimpleInfo myParticipationSimpleInfo2 =
        getStubbedParticipationByStatus(PARTICIPATION_ID + 1,
            getStubbedMember(PARTICIPANT_ID + 1), STUBBED_GATHERING,
            Status.APPROVED).toMyParticipationSimpleInfo();

    given(participationService.readMyParticipations(anyLong(), any(Pageable.class)))
        .willReturn(new PageImpl<MyParticipationSimpleInfo>(
            List.of(myParticipationSimpleInfo1, myParticipationSimpleInfo2), PAGEABLE, 2));

    //when
    //then

    mockMvc.perform(get(BASE_URL + "/mylist")
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.totalElements").value(2))
        .andExpect(jsonPath("$.totalPages").value(1))
        .andExpect(jsonPath("$.pageable.pageNumber").value(0))
        .andExpect(jsonPath("$.content[0].id").value(myParticipationSimpleInfo1.id()))
        .andExpect(
            jsonPath("$.content[0].title").value(myParticipationSimpleInfo1.title()))
        .andExpect(
            jsonPath("$.content[0].status").value(myParticipationSimpleInfo1.status().toString()))
        .andExpect(jsonPath("$.content[0].updatedDttm")
            .value(myParticipationSimpleInfo1.updatedDttm()
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
        .andExpect(jsonPath("$.content[1].id").value(myParticipationSimpleInfo2.id()))
        .andExpect(
            jsonPath("$.content[1].title").value(myParticipationSimpleInfo2.title()))
        .andExpect(jsonPath("$.content[1].status").value(
            myParticipationSimpleInfo2.status().toString()))
        .andExpect(jsonPath("$.content[1].updatedDttm")
            .value(myParticipationSimpleInfo2.updatedDttm()
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
  }

  @Test
  @WithMockCustomUser
  void failToGetMyParticipationsByAuthorizationIssue() throws Exception {
    //given
    given(participationService.readMyParticipations(anyLong(), any(Pageable.class)))
        .willThrow(new CustomException(ExceptionCode.AUTHORIZATION_ISSUE));

    //when
    //then
    mockMvc.perform(get(BASE_URL + "/mylist")
            .contentType(MediaType.APPLICATION_JSON)
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isForbidden());
  }

}