package com.withus.withmebe.comment.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import com.withus.withmebe.comment.dto.request.AddCommentRequest;
import com.withus.withmebe.comment.dto.request.SetCommentRequest;
import com.withus.withmebe.comment.dto.response.CommentResponse;
import com.withus.withmebe.comment.repository.CommentRepository;
import com.withus.withmebe.common.exception.CustomException;
import com.withus.withmebe.common.exception.ExceptionCode;
import com.withus.withmebe.member.entity.Member;
import com.withus.withmebe.member.repository.MemberRepository;
import com.withus.withmebe.member.type.Gender;
import com.withus.withmebe.security.util.MySecurityUtil;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

  @Mock
  private CommentRepository commentRepository;

  @Mock
  private MemberRepository memberRepository;

  @InjectMocks
  private CommentService commentService;


  private static final Member requester = Member.builder()
      .email("honggildong@never.com")
      .password("12345")
      .nickName("홍길동")
      .birthDate(LocalDate.of(2001, 2, 3))
      .gender(Gender.MALE)
      .signupDttm(LocalDateTime.of(2002, 3, 4, 5, 6))
      .build();
  private static final long gatheringId = 13L;
  private static final long memberId = 14L;
  private static final AddCommentRequest addCommentRequest = new AddCommentRequest("댓글내용");
  private static final SetCommentRequest setCommentRequest = new SetCommentRequest("수정내용");
  private static final LocalDateTime createdDttm = LocalDateTime.of(2003, 4, 5, 6, 7);
  private static final LocalDateTime updatedDttm = LocalDateTime.of(2004, 5, 6, 7, 8);

  @Test
  void succeessToCreateComment() throws NullPointerException {
//    //given
//    given(memberRepository.findById(anyLong()))
//        .willReturn(Optional.of(requester));
//    given(commentRepository.save(any()))
//        .willReturn(addCommentRequest.toEntity(gatheringId, memberId));
//    //when
//    CommentResponse commentResponse = commentService.createComment(gatheringId, addCommentRequest);
//
//    //then
//    assertEquals(memberId, commentResponse.id());
//    assertEquals(requester.getNickName(), commentResponse.nickName());
  }

  @Test
  void failToCreateCommentByFailedToReadMember() {
//    //given
//    given(memberRepository.findById(anyLong()))
//        .willReturn(Optional.empty());
//    //when
//    CustomException exception = assertThrows(CustomException.class,
//        () -> commentService.createComment(memberId, addCommentRequest));
//    //then
//    assertEquals(ExceptionCode.ENTITY_NOT_FOUND.getMessage(), exception.getMessage());
//    assertEquals(ExceptionCode.ENTITY_NOT_FOUND.getStatus(), exception.getHttpStatus());
  }

  // TODO 모임조회 실패 테스트
  // TODO 멤버조회 실패 테스트

}