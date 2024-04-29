package com.withus.withmebe.comment.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.withus.withmebe.comment.dto.request.AddCommentRequest;
import com.withus.withmebe.comment.dto.response.CommentResponse;
import com.withus.withmebe.comment.entity.Comment;
import com.withus.withmebe.comment.repository.CommentRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

  @Mock
  private CommentRepository commentRepository;

  @InjectMocks
  private CommentService commentService;

  @Test
  void succeessToCreateComment() {
    //given
    // TODO 모임 유효성 검사 패스
    // TODO 멤버 유효성 검사 패스
    // TODO 멤버 가져오기
    long gatheringId = 2L;
    long memberId = 3l;
    AddCommentRequest request = new AddCommentRequest("내용");
    LocalDateTime createdDttm = LocalDateTime.now();

    given(commentRepository.save(any()))
        .willReturn(Comment.builder()
            .id(1L)
            .gatheringId(gatheringId)
            .memberId(memberId)
            .content(request.getContent())
            .createdDttm(createdDttm)
            .build()
        );// TODO 멤버 정보 추후 추가.
    //when
    CommentResponse commentResponse = commentService.createComment(gatheringId, request);

    //then
    assertEquals(1L, commentResponse.getId());
    // assertEquals(member.getNickName(), commentResponse.getNick_Name());
    assertEquals(request.getContent(), commentResponse.getContent());
    assertEquals(createdDttm, commentResponse.getCreatedDttm());
    assertNull(commentResponse.getModifiedDttm());
  }
  
  // TODO 모임조회 실패 테스트
  // TODO 멤버조회 실패 테스트
  
}