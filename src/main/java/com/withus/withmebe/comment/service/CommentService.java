package com.withus.withmebe.comment.service;

import static com.withus.withmebe.common.exception.ExceptionCode.AUTHORIZATION_ISSUE;
import static com.withus.withmebe.common.exception.ExceptionCode.ENTITY_NOT_FOUND;

import com.withus.withmebe.comment.dto.request.SetCommentRequest;
import com.withus.withmebe.comment.dto.response.CommentResponse;
import com.withus.withmebe.comment.entity.Comment;
import com.withus.withmebe.comment.repository.CommentRepository;
import com.withus.withmebe.comment.dto.request.AddCommentRequest;
import com.withus.withmebe.common.exception.CustomException;
import com.withus.withmebe.member.entity.Member;
import com.withus.withmebe.member.repository.MemberRepository;
import com.withus.withmebe.security.util.MySecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

  private final CommentRepository commentRepository;
  private final MemberRepository memberRepository;

  @Transactional
  public CommentResponse createComment(long gatheringId, AddCommentRequest request) {

    Member requester = readRequester();
    Comment newComment = commentRepository.save(request.toEntity(gatheringId, requester));
    return CommentResponse.fromEntity(newComment);
  }

  @Transactional(readOnly = true)
  public Page<CommentResponse> readComments(long gatheringId, Pageable pageable) {

    Pageable adjustedPageable = adjustPageable(pageable);
    Page<Comment> comments = commentRepository.findCommentsByGatheringId(gatheringId, adjustedPageable);
    return comments.map(CommentResponse::fromEntity);
  }

  private Pageable adjustPageable(Pageable pageable) {

    int size = Math.max(pageable.getPageSize(), 1);
    int page = Math.max(pageable.getPageNumber(), 0);
    return PageRequest.of(page, size);
  }

  @Transactional
  public CommentResponse updateComment(long commentId, SetCommentRequest request) {

    Comment comment = readEditableComment(commentId);

    comment.setCommentContent(request.commentContent());
    Comment updatedComment = readComment(commentId);
    return CommentResponse.fromEntity(updatedComment);
  }

  @Transactional
  public CommentResponse deleteComment(long commentId) {

    Comment comment = readEditableComment(commentId);
    commentRepository.delete(comment);
    return CommentResponse.fromEntity(comment);
  }

  private Comment readEditableComment(long commentId) {
    Comment comment = readComment(commentId);

    if (comment.getMember().getId() != getRequesterId()) {
      throw new CustomException(AUTHORIZATION_ISSUE);
    }
    return comment;
  }

  private Comment readComment(long commentId) {
    return commentRepository.findById(commentId)
        .orElseThrow(() -> new CustomException(ENTITY_NOT_FOUND));
  }

  private Member readRequester() {
    return memberRepository.findById(getRequesterId())
        .orElseThrow(() -> new CustomException(ENTITY_NOT_FOUND));
  }

  private long getRequesterId() {
    String memberId = MySecurityUtil.getCustomUserDetails().getUsername();
    return Long.parseLong(memberId);
  }
}
