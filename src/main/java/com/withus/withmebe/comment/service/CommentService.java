package com.withus.withmebe.comment.service;

import com.withus.withmebe.comment.dto.response.CommentResponse;
import com.withus.withmebe.comment.entity.Comment;
import com.withus.withmebe.comment.repository.CommentRepository;
import com.withus.withmebe.comment.dto.request.AddCommentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {
  private final CommentRepository commentRepository;

  @Transactional
  public CommentResponse createComment(long gatheringId, AddCommentRequest request) {
    // TODO 모임 유효성 검사
    // TODO AuthContext에서 멤버ID 획득 추가
    // TODO 멤버 유효성 검사
    // TODO 멤버 받기
    long memberId = 1L; // TODO member.getId으로 교체

    // TODO 멤버ID 유효성 검사

    Comment newComment = commentRepository.save(Comment.fromRequest(gatheringId, memberId, request));
    return CommentResponse.builder()
        .id(newComment.getId())
        .nick_Name("홍길동") // TODO member.getNickName으로 교체
        .content(request.getContent())
        .createdDttm(newComment.getCreatedDttm())
        .modifiedDttm(newComment.getModifiedDttm())
        .build();
  }
}
