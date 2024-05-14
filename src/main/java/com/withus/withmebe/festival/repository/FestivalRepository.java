package com.withus.withmebe.festival.repository;

import com.withus.withmebe.festival.entity.Festival;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FestivalRepository extends JpaRepository<Festival, Long> {

  List<Festival> findFestivalByStartDttmBeforeAndEndDttmAfter(LocalDateTime currentTime1, LocalDateTime currentTime2);
}
