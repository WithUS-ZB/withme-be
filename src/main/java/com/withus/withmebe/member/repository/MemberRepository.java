package com.withus.withmebe.member.repository;

import com.withus.withmebe.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
  boolean existsByEmail(String email);

  Optional<Member> findByEmail(String email);
}
