package com.withus.withmebe.festival.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static util.objectprovider.FestivalProvider.getStubbedFestival;

import com.withus.withmebe.festival.dto.FestivalSimpleInfo;
import com.withus.withmebe.festival.entity.Festival;
import com.withus.withmebe.festival.repository.FestivalRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class FestivalServiceTest {

  @Mock
  private FestivalRepository festivalRepository;

  @InjectMocks
  private FestivalService festivalService;

  @Test
  void successToReadFestivals() {
    //given
    Festival festival1 = getStubbedFestival(1L);
    Festival festival2 = getStubbedFestival(2L);
    Pageable pageable = Pageable.ofSize(6);

    given(festivalRepository.findFestivalByStartDttmBeforeAndEndDttmAfter(any(LocalDateTime.class), any(LocalDateTime.class), eq(pageable)))
        .willReturn(new PageImpl<>(List.of(festival1, festival2),pageable,2));

    //when
    Page<FestivalSimpleInfo> festivalSimpleInfos = festivalService.readFestivals(pageable);

    //then
    assertEquals(2, festivalSimpleInfos.getTotalElements());
    assertEquals(1, festivalSimpleInfos.getTotalPages());
    assertEquals(0, festivalSimpleInfos.getNumber());

    FestivalSimpleInfo festivalSimpleInfo1 = festivalSimpleInfos.getContent().get(0);
    assertEquals(festival1.getTitle(), festivalSimpleInfo1.title());
    assertEquals(festival1.getImg(), festivalSimpleInfo1.img());
    assertEquals(festival1.getLink(), festivalSimpleInfo1.link());

    FestivalSimpleInfo festivalSimpleInfo2 = festivalSimpleInfos.getContent().get(1);
    assertEquals(festival2.getTitle(), festivalSimpleInfo2.title());
    assertEquals(festival2.getImg(), festivalSimpleInfo2.img());
    assertEquals(festival2.getLink(), festivalSimpleInfo2.link());
  }
}