package com.withus.withmebe.festival.repository;

import com.withus.withmebe.festival.entity.Festival;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FestivalRepository extends JpaRepository<Festival, Long> {

  Page<Festival> findFestivalByStartDttmBeforeAndEndDttmAfter(LocalDateTime currentTime1, LocalDateTime currentTime2, Pageable pageable);
}
