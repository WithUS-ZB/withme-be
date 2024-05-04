package com.withus.withmebe.comment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import com.withus.withmebe.comment.dto.request.AddCommentRequest;
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
    Comment comment = getStubbedComment(commentId, gatheringId, requester, request);

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


  private Member getStubbedMember(long memberId) {
    Member member = Member.builder()
        .nickName("홍길동" + memberId)
        .build();
    ReflectionTestUtils.setField(member, "id", memberId);
    return member;
  }

  private Comment getStubbedComment(long commentId, long gatheringId, Member member,
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