package com.withus.withmebe.member.repository;

import com.withus.withmebe.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
  boolean existsByEmail(String email);
  boolean existsByNickName(String nickName);

  Optional<Member> findByEmail(String email);
}
