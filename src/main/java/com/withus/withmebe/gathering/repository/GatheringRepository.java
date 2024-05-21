package com.withus.withmebe.gathering.repository;

import com.withus.withmebe.gathering.entity.Gathering;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GatheringRepository extends JpaRepository<Gathering, Long> {
    @EntityGraph(attributePaths = "member")
    Page<Gathering> findAllByOrderByCreatedDttmDesc(Pageable pageable);

    @EntityGraph(attributePaths = "member")
    Page<Gathering> findAllByMemberId(long currentMemberId, Pageable pageable);
}
