package com.withus.withmebe.comment.repository;

import com.withus.withmebe.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

  @EntityGraph(attributePaths = "writer")
  Page<Comment> findCommentsByGatheringId(long gatheringId, Pageable pageable);

}
