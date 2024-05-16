package com.withus.withmebe.gathering.repository;

import com.withus.withmebe.gathering.entity.Gathering;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GatheringRepository extends JpaRepository<Gathering, Long> {

    List<Gathering> findAllByOrderByCreatedDttmDesc();
    Optional<Gathering> findById(Long aLong);

}
