package com.withus.withmebe.comment.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.withus.withmebe.comment.entity.Comment;
import com.withus.withmebe.member.entity.Member;
import com.withus.withmebe.member.repository.MemberRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
@Sql(scripts = "classpath:comment/testdata.sql")
@AutoConfigureTestDatabase(replace = Replace.NONE)
class CommentRepositoryTest {

  @Autowired
  private CommentRepository commentRepository;

  @Autowired
  private MemberRepository memberRepository;

  private final long gatheringId = 1L;

  @Test
  void readCommentsByGatheringId() {
    //given

    Pageable pageable = PageRequest.of(0,10);
    //when
    Page<Comment> comments = commentRepository.findCommentsByGatheringId(gatheringId, pageable);

    //then

    assertEquals(2, comments.getTotalElements());
    assertEquals(1, comments.getTotalPages());

    Comment pageElement1 = comments.getContent().get(0);
    assertTrue(pageElement1.getId() != null && pageElement1.getId() > 0);
    assertEquals(gatheringId, pageElement1.getGatheringId());
    assertEquals(1L, pageElement1.getMember().getId());
    assertEquals("첫 번째 댓글입니다.", pageElement1.getCommentContent());
    assertNotNull(pageElement1.getCreatedDttm());
    assertNotNull(pageElement1.getUpdatedDttm());
    assertNull(pageElement1.getDeletedDttm());

    Comment pageElement2 = comments.getContent().get(1);
    assertTrue(pageElement2.getId() != null && pageElement2.getId() > 0);
    assertEquals(gatheringId, pageElement2.getGatheringId());
    assertEquals(2L, pageElement2.getMember().getId());
    assertEquals("두 번째 댓글입니다.", pageElement2.getCommentContent());
    assertNotNull(pageElement2.getCreatedDttm());
    assertNotNull(pageElement2.getUpdatedDttm());
    assertNull(pageElement2.getDeletedDttm());
  }

}