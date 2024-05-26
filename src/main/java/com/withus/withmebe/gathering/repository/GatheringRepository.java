package com.withus.withmebe.gathering.repository;

import com.withus.withmebe.gathering.Type.GatheringType;
import com.withus.withmebe.gathering.Type.Status;
import com.withus.withmebe.gathering.entity.Gathering;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GatheringRepository extends JpaRepository<Gathering, Long> {

    @EntityGraph(attributePaths = "member")
    Page<Gathering> findAllBy(Pageable pageable);

    @EntityGraph(attributePaths = "member")
    Page<Gathering> findAllByGatheringType(GatheringType type, Pageable pageable);

    @EntityGraph(attributePaths = "member")
    Page<Gathering> findAllByMemberId(long currentMemberId, Pageable pageable);

    List<Gathering> findAllByDayAndStatusEquals(LocalDate day, Status status);

    int countByMemberId(long memberId);
}
