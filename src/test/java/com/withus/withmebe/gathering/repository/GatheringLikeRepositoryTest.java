package com.withus.withmebe.gathering.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.withus.withmebe.gathering.entity.GatheringLike;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@ActiveProfiles("test")
@Sql(scripts = "classpath:/sql/gathering/likedata.sql")
@AutoConfigureTestDatabase(replace = Replace.NONE)
class GatheringLikeRepositoryTest {

  @Autowired
  private GatheringLikeRepository gatheringLikeRepository;

  private static final long GATHERING_ID = 1L;
  private static final long MEMBER_ID = 1L;
  @Test
  void successToFindByMemberIdAndGatheringId() {
    //given
    //when
    Optional<GatheringLike> optionalGatheringLike = gatheringLikeRepository
        .findByMemberIdAndGathering_Id(MEMBER_ID, GATHERING_ID);

    //then
    GatheringLike gatheringLike = optionalGatheringLike.get();

    assertNotNull(gatheringLike.getGathering());
    assertEquals(MEMBER_ID, gatheringLike.getMemberId());
    assertEquals(GATHERING_ID, gatheringLike.getGathering().getId());
    assertNotNull(gatheringLike.getCreatedDttm());
    assertNotNull(gatheringLike.getUpdatedDttm());
    assertNull(gatheringLike.getDeletedDttm());
  }

  @Test
  void successToCountByGatheringIdAndIsLikedIsTrue() {
    //given
    //when
    //then
    assertEquals(1L, gatheringLikeRepository.countGatheringLikesByGathering_IdAndIsLikedIsTrue(GATHERING_ID));
  }
}