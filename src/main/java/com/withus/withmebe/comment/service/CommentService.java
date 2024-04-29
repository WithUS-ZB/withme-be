package com.withus.withmebe.comment.service;

import com.withus.withmebe.comment.dto.response.CommentResponse;
import com.withus.withmebe.comment.entity.Comment;
import com.withus.withmebe.comment.repository.CommentRepository;
import com.withus.withmebe.comment.dto.request.AddCommentRequest;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    String memberNickName = "홍길동";

    Comment newComment = commentRepository.save(
        Comment.fromRequest(gatheringId, memberId, request));

    return CommentResponse.fromEntity(newComment, memberNickName);
  }

  @Transactional(readOnly = true)
  public Page<CommentResponse> readComments(long gatheringId, Pageable pageable) {

    Pageable adjustedPageable = adjustPageable(pageable);
    Page<Comment> comments = commentRepository.findCommentsByGatheringIdAndDeletedDttmIsNull(
        gatheringId, adjustedPageable);

    List<CommentResponse> commentResponses = new ArrayList<>();
    for (Comment comment : comments) {
      // TODO 멤버 아이디로 멤버 이름 가져오기
      String memberName = "홍길동";
      commentResponses.add(CommentResponse.fromEntity(comment, memberName));
    }
    return new PageImpl<CommentResponse>(commentResponses, adjustedPageable, commentResponses.size());
  }

  private Pageable adjustPageable(Pageable pageable) {
    int size = pageable.getPageSize();
    int page = pageable.getPageNumber();

    if (size < 1) {
      size = 1;
    }
    if (page < 0) {
      page = 0;
    }

    return PageRequest.of(page, size);
  }
}
