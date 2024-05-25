package com.withus.withmebe.festival.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.withus.withmebe.festival.entity.Festival;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@ActiveProfiles("test")
@Sql(scripts = "classpath:/sql/festival/data.sql")
@AutoConfigureTestDatabase(replace = Replace.NONE)
class FestivalRepositoryTest {

  @Autowired
  private FestivalRepository festivalRepository;

  @Test
  void successToFindFestivalByStartDttmBeforeAndEndDttmAfter() {
    //given
    LocalDateTime now = LocalDateTime.now();

    //when

    Page<Festival> festivals = festivalRepository.findFestivalByStartDttmBeforeAndEndDttmAfter(now, now, Pageable.ofSize(6));

    //then
    assertEquals(2, festivals.getTotalElements());
    assertEquals(1, festivals.getTotalPages());
    assertEquals(0, festivals.getNumber());

    Festival festival1 = festivals.getContent().get(0);
    assertEquals(1L, festival1.getId());
    assertEquals("title1", festival1.getTitle());
    assertEquals("img1", festival1.getImg());
    assertEquals("link1", festival1.getLink());
    assertTrue(festival1.getStartDttm().isBefore(now));
    assertTrue(festival1.getEndDttm().isAfter(now));
    assertNotNull(festival1.getCreatedDttm());
    assertNotNull(festival1.getUpdatedDttm());
    assertNull(festival1.getDeletedDttm());

    Festival festival2 = festivals.getContent().get(1);
    assertEquals(2L, festival2.getId());
    assertEquals("title2", festival2.getTitle());
    assertEquals("img2", festival2.getImg());
    assertEquals("link2", festival2.getLink());
    assertTrue(festival2.getStartDttm().isBefore(now));
    assertTrue(festival2.getEndDttm().isAfter(now));
    assertNotNull(festival2.getCreatedDttm());
    assertNotNull(festival2.getUpdatedDttm());
    assertNull(festival2.getDeletedDttm());

  }
}