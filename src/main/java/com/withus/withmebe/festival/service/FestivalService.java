package com.withus.withmebe.festival.service;

import com.withus.withmebe.festival.dto.FestivalSimpleInfo;
import com.withus.withmebe.festival.entity.Festival;
import com.withus.withmebe.festival.repository.FestivalRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FestivalService {
  private final FestivalRepository festivalRepository;

  @Transactional(readOnly = true)
  public List<FestivalSimpleInfo> readFestivals() {
    List<Festival> festivals = festivalRepository.findFestivalByStartDttmBeforeAndEndDttmAfter(LocalDateTime.now(), LocalDateTime.now()) ;
    return festivals.stream().map(Festival::toFestivalSimpleInfo).collect(Collectors.toList());
  }
}
