package com.withus.withmebe.comment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import com.withus.withmebe.comment.dto.request.AddCommentRequest;
import com.withus.withmebe.comment.dto.request.SetCommentRequest;
import com.withus.withmebe.comment.dto.response.CommentResponse;
import com.withus.withmebe.comment.entity.Comment;
import com.withus.withmebe.comment.repository.CommentRepository;
import com.withus.withmebe.common.exception.CustomException;
import com.withus.withmebe.common.exception.ExceptionCode;
import com.withus.withmebe.member.entity.Member;
import com.withus.withmebe.member.repository.MemberRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

  @Mock
  private MemberRepository memberRepository;

  @Mock
  private CommentRepository commentRepository;

  @InjectMocks
  private CommentService commentService;

  private final long memberId = 1L;
  private final long gatheringId = 1L;

  private final long commentId = 1L;

  @Test
  void seccessToCreateComment() {
    //given
    AddCommentRequest request = new AddCommentRequest("댓글");
    Member requester = getStubbedMember(memberId);
    Comment comment = getStubbedNewComment(commentId, gatheringId, requester, request);

    given(memberRepository.findById(anyLong()))
        .willReturn(Optional.of(new Member()));
    given(commentRepository.save(any()))
        .willReturn(comment);

    //when
    CommentResponse commentResponse = commentService.createComment(memberId, gatheringId,
        request);

    //then
    assertEquals(commentId, commentResponse.id());
    assertEquals(requester.getNickName(), commentResponse.nickName());
    assertEquals(request.commentContent(), commentResponse.commentContent());
    assertNotNull(commentResponse.createdDttm());
    assertNotNull(commentResponse.updatedDttm());
  }

  @Test
  void failToCreateCommentByFailedToReadRequester() {
    //given
    given(memberRepository.findById(anyLong()))
        .willReturn(Optional.empty());

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> commentService.createComment(memberId, gatheringId, new AddCommentRequest("댓글")));
    //then
    assertEquals(ExceptionCode.ENTITY_NOT_FOUND.getMessage(), exception.getMessage());
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
  }

  @Test
  void successToReadComments() {
    //given
    Pageable pageable = PageRequest.of(0, 10);
    Member writer1 = getStubbedMember(memberId);
    Comment comment1 = getStubbedComment(commentId, gatheringId, writer1);
    Member writer2 = getStubbedMember(memberId + 1);
    Comment comment2 = getStubbedComment(commentId + 1, gatheringId, writer2);

    given(commentRepository.findCommentsByGatheringId(gatheringId, pageable))
        .willReturn(new PageImpl<Comment>(List.of(comment1, comment2), pageable, 2));

    //when
    Page<CommentResponse> commentResponses = commentService.readComments(gatheringId, pageable);
    //then
    assertEquals(2, commentResponses.getTotalElements());
    assertEquals(1, commentResponses.getTotalPages());
    assertEquals(0, commentResponses.getNumber());

    CommentResponse commentResponse1 = commentResponses.getContent().get(0);
    assertEquals(comment1.getId(), commentResponse1.id());
    assertEquals(comment1.getMember().getNickName(), commentResponse1.nickName());
    assertEquals(comment1.getCommentContent(), commentResponse1.commentContent());
    assertEquals(comment1.getCreatedDttm(), commentResponse1.createdDttm());
    assertEquals(comment1.getUpdatedDttm(), commentResponse1.updatedDttm());

    CommentResponse commentResponse2 = commentResponses.getContent().get(1);
    assertEquals(comment2.getId(), commentResponse2.id());
    assertEquals(comment2.getMember().getNickName(), commentResponse2.nickName());
    assertEquals(comment2.getCommentContent(), commentResponse2.commentContent());
    assertEquals(comment2.getCreatedDttm(), commentResponse2.createdDttm());
    assertEquals(comment2.getUpdatedDttm(), commentResponse2.updatedDttm());
  }

  @Test
  void seccessToUpdateComment() {
    //given
    SetCommentRequest request = new SetCommentRequest("수정된 댓글");
    Member requester = getStubbedMember(memberId);
    Comment comment = getStubbedComment(commentId, gatheringId, requester);
    comment.setCommentContent(request.commentContent());

    given(commentRepository.findById(anyLong()))
        .willReturn(Optional.of(comment));

    //when
    CommentResponse commentResponse = commentService.updateComment(memberId, commentId,
        request);

    //then
    assertEquals(commentId, commentResponse.id());
    assertEquals(requester.getNickName(), commentResponse.nickName());
    assertEquals(request.commentContent(), commentResponse.commentContent());
    assertNotNull(commentResponse.createdDttm());
    assertNotNull(commentResponse.updatedDttm());
  }

  private Member getStubbedMember(long memberId) {
    Member member = Member.builder()
        .nickName("홍길동" + memberId)
        .build();
    ReflectionTestUtils.setField(member, "id", memberId);
    return member;
  }

  private Comment getStubbedNewComment(long commentId, long gatheringId, Member member,
      AddCommentRequest request) {
    Comment comment = Comment.builder()
        .member(member)
        .commentContent(request.commentContent())
        .gatheringId(gatheringId)
        .build();
    ReflectionTestUtils.setField(comment, "id", commentId);
    ReflectionTestUtils.setField(comment, "createdDttm", LocalDateTime.now());
    ReflectionTestUtils.setField(comment, "updatedDttm", LocalDateTime.now());
    return comment;
  }

  private Comment getStubbedComment(long commentId, long gatheringId, Member member) {
    Comment comment = Comment.builder()
        .member(member)
        .commentContent("댓글" + commentId)
        .gatheringId(gatheringId)
        .build();
    ReflectionTestUtils.setField(comment, "id", commentId);
    ReflectionTestUtils.setField(comment, "createdDttm", LocalDateTime.now());
    ReflectionTestUtils.setField(comment, "updatedDttm", LocalDateTime.now());
    return comment;
  }
}