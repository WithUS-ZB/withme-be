package com.withus.withmebe.comment.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.withus.withmebe.comment.entity.Comment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@ActiveProfiles("test")
@Sql(scripts = "classpath:/sql/comment/data.sql")
@AutoConfigureTestDatabase(replace = Replace.NONE)
class CommentRepositoryTest {

  @Autowired
  private CommentRepository commentRepository;

  private static final long GATHERING_ID = 1L;

  @Test
  void testToReadCommentsByGatheringId() {
    //given

    Pageable pageable = PageRequest.of(0,10);
    //when
    Page<Comment> comments = commentRepository.findCommentsByGatheringId(GATHERING_ID, pageable);

    //then

    assertEquals(2, comments.getTotalElements());
    assertEquals(1, comments.getTotalPages());
    assertEquals(0, comments.getNumber());

    Comment comment1 = comments.getContent().get(0);
    assertEquals(1L, comment1.getId());
    assertEquals(GATHERING_ID, comment1.getGatheringId());
    assertEquals(1L, comment1.getWriter().getId());
    assertEquals("comment1", comment1.getCommentContent());
    assertNotNull(comment1.getCreatedDttm());
    assertNotNull(comment1.getUpdatedDttm());
    assertNull(comment1.getDeletedDttm());

    Comment comment2 = comments.getContent().get(1);
    assertEquals(2L, comment2.getId());
    assertEquals(GATHERING_ID, comment2.getGatheringId());
    assertEquals(2L, comment2.getWriter().getId());
    assertEquals("comment2", comment2.getCommentContent());
    assertNotNull(comment2.getCreatedDttm());
    assertNotNull(comment2.getUpdatedDttm());
    assertNull(comment2.getDeletedDttm());
  }

}