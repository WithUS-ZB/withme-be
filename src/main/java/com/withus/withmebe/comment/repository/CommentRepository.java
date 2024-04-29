package com.withus.withmebe.comment.repository;

import com.withus.withmebe.comment.entity.Comment;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

  Page<Comment> findCommentsByGatheringIdAndDeletedDttmIsNull(long gatheringId, Pageable pageble);

  Optional<Comment> findByIdAndDeletedDttmIsNull(long commentId);
}
