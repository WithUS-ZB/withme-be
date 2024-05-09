package com.withus.withmebe.participation.repository;

import com.withus.withmebe.gathering.entity.Gathering;
import com.withus.withmebe.participation.entity.Participation;
import com.withus.withmebe.participation.type.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipationRepository extends JpaRepository<Participation, Long> {

  boolean existsByParticipant_IdAndStatusIsNot(Long requesterId, Status status);

  long countByGathering_IdAndStatus(Long gatheringId, Status status);

  @EntityGraph(attributePaths = "participant")
  Page<Participation> findByGathering(Gathering gathering, Pageable pageable);
}
