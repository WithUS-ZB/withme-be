package com.withus.withmebe.gathering.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static util.objectprovider.GatheringLikeProvider.getStubbedGatheringLike;
import static util.objectprovider.GatheringProvider.getStubbedGathering;

import com.withus.withmebe.common.exception.CustomException;
import com.withus.withmebe.common.exception.ExceptionCode;
import com.withus.withmebe.gathering.entity.GatheringLike;
import com.withus.withmebe.gathering.repository.GatheringLikeRepository;
import com.withus.withmebe.gathering.repository.GatheringRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;


@ExtendWith(MockitoExtension.class)
class GatheringLikeServiceTest {
  @Mock
  private GatheringLikeRepository gatheringLikeRepository;
  @Mock
  private GatheringRepository gatheringRepository;
  @InjectMocks
  private GatheringLikeService gatheringLikeService;

  private static final long MEMBER_ID = 1L;
  private static final long GATHERING_ID = 2L;

  @Test
  void successToDoLikeWhenNewLike() {
    //given
    given(gatheringLikeRepository.findByMemberIdAndGathering_Id(MEMBER_ID, GATHERING_ID))
        .willReturn(Optional.empty());
    given(gatheringRepository.findById(anyLong()))
        .willReturn(Optional.of(getStubbedGathering(GATHERING_ID, MEMBER_ID)));
    given(gatheringLikeRepository.save(any(GatheringLike.class)))
        .willAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

    //when
    boolean isLiked = gatheringLikeService.doLike(MEMBER_ID, GATHERING_ID);
    //then
    assertTrue(isLiked);
  }

  @Test
  void successToDoLikeWhenLikeWasTrue() {
    //given
    given(gatheringLikeRepository.findByMemberIdAndGathering_Id(MEMBER_ID, GATHERING_ID))
        .willReturn(Optional.of(getStubbedGatheringLike(true)));

    //when
    boolean isLiked = gatheringLikeService.doLike(MEMBER_ID, GATHERING_ID);

    //then
    assertFalse(isLiked);
  }

  @Test
  void successToDoLikeWhenLikeWasFalse() {
    //given
    given(gatheringLikeRepository.findByMemberIdAndGathering_Id(MEMBER_ID, GATHERING_ID))
        .willReturn(Optional.of(getStubbedGatheringLike(false)));

    //when
    boolean isLiked = gatheringLikeService.doLike(MEMBER_ID, GATHERING_ID);

    //then
    assertTrue(isLiked);
  }

  @Test
  void failToDoLikeByGatheringNotFound() {
    //given
    given(gatheringLikeRepository.findByMemberIdAndGathering_Id(anyLong(), anyLong()))
        .willReturn(Optional.empty());
    given(gatheringRepository.findById(anyLong()))
        .willReturn(Optional.empty());

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> gatheringLikeService.doLike(MEMBER_ID, GATHERING_ID));

    //then
    assertEquals(ExceptionCode.ENTITY_NOT_FOUND.getMessage(), exception.getMessage());
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
  }
}