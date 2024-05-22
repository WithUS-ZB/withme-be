package com.withus.withmebe.participation.repository;

import com.withus.withmebe.gathering.entity.Gathering;
import com.withus.withmebe.participation.entity.Participation;
import com.withus.withmebe.participation.type.Status;
import jakarta.persistence.LockModeType;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipationRepository extends JpaRepository<Participation, Long> {

  boolean existsByParticipant_IdAndGathering_IdAndStatusIsNot(Long requesterId,Long gatheringId, Status status);

  long countByGathering_IdAndStatus(Long gatheringId, Status status);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  long countByGatheringAndStatus(Gathering gathering, Status status);

  @EntityGraph(attributePaths = "participant")
  Page<Participation> findByGathering_Id(Long gatheringId, Pageable pageable);

  @EntityGraph(attributePaths = "gathering")
  Page<Participation> findByParticipant_Id(Long requesterId, Pageable pageable);
  List<Participation> findAllByGatheringAndStatusEquals(Gathering gathering, Status status);
}
