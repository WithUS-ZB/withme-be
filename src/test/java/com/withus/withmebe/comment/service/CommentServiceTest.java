package com.withus.withmebe.comment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static util.objectprovider.CommentProvider.getStubbedComment;
import static util.objectprovider.CommentProvider.getStubbedCommentWithAddCommentRequest;
import static util.objectprovider.MemberProvider.getStubbedMember;

import com.withus.withmebe.comment.dto.request.AddCommentRequest;
import com.withus.withmebe.comment.dto.request.SetCommentRequest;
import com.withus.withmebe.comment.dto.response.CommentResponse;
import com.withus.withmebe.comment.entity.Comment;
import com.withus.withmebe.comment.repository.CommentRepository;
import com.withus.withmebe.common.exception.CustomException;
import com.withus.withmebe.common.exception.ExceptionCode;
import com.withus.withmebe.gathering.repository.GatheringRepository;
import com.withus.withmebe.member.entity.Member;
import com.withus.withmebe.member.repository.MemberRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

  @Mock
  private MemberRepository memberRepository;

  @Mock
  private CommentRepository commentRepository;

  @Mock
  private GatheringRepository gatheringRepository;

  @InjectMocks
  private CommentService commentService;

  private static final long REQUESTER_ID = 1L;
  private static final long GATHERING_ID = 2L;
  private static final long COMMENT_ID = 3L;
  private static final Pageable PAGEABLE = Pageable.ofSize(10);

  @Test
  void successToCreateComment() {
    //given
    Member requester = getStubbedMember(REQUESTER_ID);
    AddCommentRequest request = new AddCommentRequest("댓글");
    Comment comment = getStubbedCommentWithAddCommentRequest(COMMENT_ID, GATHERING_ID, requester, request);

    given(gatheringRepository.existsById(GATHERING_ID))
        .willReturn(true);
    given(memberRepository.findById(anyLong()))
        .willAnswer(invocationOnMock -> {
          long memberId = invocationOnMock.getArgument(0);
          return Optional.of(getStubbedMember(memberId));
            });
    given(commentRepository.save(any(Comment.class)))
        .willReturn(comment);

    //when
    CommentResponse commentResponse = commentService.createComment(REQUESTER_ID, GATHERING_ID,
        request);

    //then
    assertEquals(COMMENT_ID, commentResponse.id());
    assertEquals(requester.getNickName(), commentResponse.nickName());
    assertEquals(requester.getProfileImg(), commentResponse.profileImg());
    assertEquals(request.commentContent(), commentResponse.commentContent());
    assertNotNull(commentResponse.createdDttm());
    assertNotNull(commentResponse.updatedDttm());
  }

  @Test
  void failToCreateCommentByFailedToReadRequester() {
    //given
    given(gatheringRepository.existsById(GATHERING_ID))
        .willReturn(true);
    given(memberRepository.findById(anyLong()))
        .willReturn(Optional.empty());

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> commentService.createComment(REQUESTER_ID, GATHERING_ID, new AddCommentRequest("댓글")));

    //then
    assertEquals(ExceptionCode.ENTITY_NOT_FOUND.getMessage(), exception.getMessage());
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
  }

  @Test
  void failToCreateCommentByGatheringNotExists() {
    //given
    given(gatheringRepository.existsById(GATHERING_ID))
        .willReturn(false);

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> commentService.createComment(REQUESTER_ID, GATHERING_ID, new AddCommentRequest("댓글")));

    //then
    assertEquals(ExceptionCode.ENTITY_NOT_FOUND.getMessage(), exception.getMessage());
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
  }

  @Test
  void successToReadComments() {
    //given
    Member writer1 = getStubbedMember(REQUESTER_ID);
    Comment comment1 = getStubbedComment(COMMENT_ID, GATHERING_ID, writer1);
    Member writer2 = getStubbedMember(REQUESTER_ID + 1);
    Comment comment2 = getStubbedComment(COMMENT_ID + 1, GATHERING_ID, writer2);

    given(commentRepository.findCommentsByGatheringId(GATHERING_ID, PAGEABLE))
        .willReturn(new PageImpl<Comment>(List.of(comment1, comment2), PAGEABLE, 2));

    //when
    Page<CommentResponse> commentResponses = commentService.readComments(GATHERING_ID, PAGEABLE);
    //then
    assertEquals(2, commentResponses.getTotalElements());
    assertEquals(1, commentResponses.getTotalPages());
    assertEquals(0, commentResponses.getNumber());

    CommentResponse commentResponse1 = commentResponses.getContent().get(0);
    assertEquals(comment1.getId(), commentResponse1.id());
    assertEquals(comment1.getWriter().getNickName(), commentResponse1.nickName());
    assertEquals(comment1.getWriter().getProfileImg(), commentResponse1.profileImg());
    assertEquals(comment1.getCommentContent(), commentResponse1.commentContent());
    assertEquals(comment1.getCreatedDttm(), commentResponse1.createdDttm());
    assertEquals(comment1.getUpdatedDttm(), commentResponse1.updatedDttm());

    CommentResponse commentResponse2 = commentResponses.getContent().get(1);
    assertEquals(comment2.getId(), commentResponse2.id());
    assertEquals(comment2.getWriter().getNickName(), commentResponse2.nickName());
    assertEquals(comment2.getWriter().getProfileImg(), commentResponse2.profileImg());
    assertEquals(comment2.getCommentContent(), commentResponse2.commentContent());
    assertEquals(comment2.getCreatedDttm(), commentResponse2.createdDttm());
    assertEquals(comment2.getUpdatedDttm(), commentResponse2.updatedDttm());
  }

  @Test
  void successToUpdateComment() {
    //given
    SetCommentRequest request = new SetCommentRequest("수정된 댓글");
    Member writer = getStubbedMember(REQUESTER_ID);
    Comment comment = getStubbedComment(COMMENT_ID, GATHERING_ID, writer);

    given(commentRepository.findById(anyLong()))
        .willReturn(Optional.of(comment));

    //when
    CommentResponse commentResponse = commentService.updateComment(REQUESTER_ID, COMMENT_ID,
        request);

    //then
    assertEquals(COMMENT_ID, commentResponse.id());
    assertEquals(writer.getNickName(), commentResponse.nickName());
    assertEquals(writer.getProfileImg(), commentResponse.profileImg());
    assertEquals(request.commentContent(), commentResponse.commentContent());
    assertNotNull(commentResponse.createdDttm());
    assertNotNull(commentResponse.updatedDttm());
  }

  @Test
  void failToUpdateCommentByCommentNotFound() {
    //given
    given(commentRepository.findById(anyLong()))
        .willReturn(Optional.empty());

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> commentService.updateComment(REQUESTER_ID, COMMENT_ID, new SetCommentRequest("수정")));

    //then
    assertEquals(ExceptionCode.ENTITY_NOT_FOUND.getMessage(), exception.getMessage());
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
  }

  @Test
  void failToUpdateCommentByRequesterIsNotWriter() {
    //given
    Comment comment = getStubbedComment(COMMENT_ID, GATHERING_ID, getStubbedMember(REQUESTER_ID));

    given(commentRepository.findById(anyLong()))
        .willReturn(Optional.of(comment));

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> commentService.updateComment(REQUESTER_ID + 1, COMMENT_ID, new SetCommentRequest("수정")));

    //then
    assertEquals(ExceptionCode.AUTHORIZATION_ISSUE.getMessage(), exception.getMessage());
    assertEquals(HttpStatus.FORBIDDEN, exception.getHttpStatus());
  }

  @Test
  void successToDeleteComment() {
    //given
    Member writer = getStubbedMember(REQUESTER_ID);
    Comment comment = getStubbedComment(COMMENT_ID, GATHERING_ID, writer);

    given(commentRepository.findById(anyLong()))
        .willReturn(Optional.of(comment));

    //when
    CommentResponse commentResponse = commentService.deleteComment(REQUESTER_ID, COMMENT_ID);

    //then
    assertEquals(COMMENT_ID, commentResponse.id());
    assertEquals(writer.getNickName(), commentResponse.nickName());
    assertEquals(writer.getProfileImg(), commentResponse.profileImg());
    assertEquals(comment.getCommentContent(), commentResponse.commentContent());
    assertNotNull(commentResponse.createdDttm());
    assertNotNull(commentResponse.updatedDttm());
  }

  @Test
  void failToDeleteCommentByCommentNotFound() {
    //given
    given(commentRepository.findById(anyLong()))
        .willReturn(Optional.empty());

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> commentService.deleteComment(REQUESTER_ID, COMMENT_ID));

    //then
    assertEquals(ExceptionCode.ENTITY_NOT_FOUND.getMessage(), exception.getMessage());
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
  }

  @Test
  void failToDeleteCommentByRequesterIsNotWriter() {
    //given
    Member writer = getStubbedMember(REQUESTER_ID);
    Comment comment = getStubbedComment(COMMENT_ID, GATHERING_ID, writer);

    given(commentRepository.findById(anyLong()))
        .willReturn(Optional.of(comment));

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> commentService.deleteComment(REQUESTER_ID + 1, COMMENT_ID));

    //then
    assertEquals(ExceptionCode.AUTHORIZATION_ISSUE.getMessage(), exception.getMessage());
    assertEquals(HttpStatus.FORBIDDEN, exception.getHttpStatus());
  }
}