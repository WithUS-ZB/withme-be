package com.withus.withmebe.participation.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.withus.withmebe.participation.entity.Participation;
import com.withus.withmebe.participation.type.Status;
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
@Sql(scripts = "classpath:sql/participation/data.sql")
@AutoConfigureTestDatabase(replace = Replace.NONE)
class ParticipationRepositoryTest {

  @Autowired
  private ParticipationRepository participationRepository;

  private static final long PARTICIPANT_ID = 2L;
  private static final long GATHERING_ID = 1L;
  private final Pageable PAGEABLE = Pageable.ofSize(10);

  @Test
  void testToExistsByParticipant_IdAndStatusIsNot() {
    //given
    //when
    //then
    assertTrue(
        participationRepository.existsByParticipant_IdAndGathering_IdAndStatusIsNot(PARTICIPANT_ID,
            GATHERING_ID, Status.CANCELED));
    assertFalse(
        participationRepository.existsByParticipant_IdAndGathering_IdAndStatusIsNot(PARTICIPANT_ID,
            GATHERING_ID, Status.APPROVED));
  }

  @Test
  void testToCountByGathering_IdAndStatus() {
    //given
    //when
    //then
    assertEquals(2L,
        participationRepository.countByGathering_IdAndStatus(GATHERING_ID, Status.APPROVED));
    assertEquals(0L,
        participationRepository.countByGathering_IdAndStatus(GATHERING_ID, Status.CREATED));
  }

  @Test
  void testToFindByGathering_Id() {
    //given
    //when
    Page<Participation> participations = participationRepository.findByGathering_Id(GATHERING_ID,
        PAGEABLE);

    //then
    assertEquals(2L, participations.getTotalElements());
    assertEquals(1L, participations.getTotalPages());
    assertEquals(0L, participations.getNumber());

    Participation participation1 = participations.getContent().get(0);
    assertEquals(1L, participation1.getId());
    assertEquals(GATHERING_ID, participation1.getGathering().getId());
    assertEquals(2L, participation1.getParticipant().getId());
    assertEquals(Status.APPROVED, participation1.getStatus());
    assertNotNull(participation1.getCreatedDttm());
    assertNotNull(participation1.getUpdatedDttm());
    assertNull(participation1.getDeletedDttm());

    Participation participation2 = participations.getContent().get(1);
    assertEquals(2L, participation2.getId());
    assertEquals(GATHERING_ID, participation2.getGathering().getId());
    assertEquals(3L, participation2.getParticipant().getId());
    assertEquals(Status.APPROVED, participation2.getStatus());
    assertNotNull(participation2.getCreatedDttm());
    assertNotNull(participation2.getUpdatedDttm());
    assertNull(participation2.getDeletedDttm());
  }

  @Test
  void testToFindByParticipant_Id() {
    //given
    //when
    Page<Participation> participations = participationRepository.findByParticipant_IdAndStatusIsNot(
        PARTICIPANT_ID, Status.CANCELED, PAGEABLE);

    //then
    assertEquals(2L, participations.getTotalElements());
    assertEquals(1L, participations.getTotalPages());
    assertEquals(0L, participations.getNumber());

    Participation participation1 = participations.getContent().get(0);
    assertEquals(1L, participation1.getId());
    assertEquals(1L, participation1.getGathering().getId());
    assertEquals(PARTICIPANT_ID, participation1.getParticipant().getId());
    assertEquals(Status.APPROVED, participation1.getStatus());
    assertNotNull(participation1.getCreatedDttm());
    assertNotNull(participation1.getUpdatedDttm());
    assertNull(participation1.getDeletedDttm());

    Participation participation2 = participations.getContent().get(1);
    assertEquals(4L, participation2.getId());
    assertEquals(2L, participation2.getGathering().getId());
    assertEquals(PARTICIPANT_ID, participation2.getParticipant().getId());
    assertEquals(Status.REJECTED, participation2.getStatus());
    assertNotNull(participation2.getCreatedDttm());
    assertNotNull(participation2.getUpdatedDttm());
    assertNull(participation2.getDeletedDttm());
  }
}