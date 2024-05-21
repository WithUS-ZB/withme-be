package com.withus.withmebe.gathering.repository;

import com.withus.withmebe.gathering.entity.Gathering;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GatheringRepository extends JpaRepository<Gathering, Long> {
    @EntityGraph(attributePaths = "member", type = EntityGraph.EntityGraphType.FETCH)
    List<Gathering> findAllByOrderByCreatedDttmDesc();

    @EntityGraph(attributePaths = "member", type = EntityGraph.EntityGraphType.FETCH)
    List<Gathering> findAllByMemberId(long currentMemberId);
}
