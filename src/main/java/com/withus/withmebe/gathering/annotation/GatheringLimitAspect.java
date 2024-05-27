package com.withus.withmebe.gathering.annotation;

import com.withus.withmebe.gathering.repository.GatheringRepository;
import com.withus.withmebe.member.entity.Member;
import com.withus.withmebe.member.repository.MemberRepository;
import com.withus.withmebe.member.type.Membership;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Aspect
@Component
@RequiredArgsConstructor
public class GatheringLimitAspect {

  private static final String EXCEED_LIMIT_MESSAGE = "게시글 등록 수가 %d개를 초과하여 더 이상 게시글을 등록할 수 없습니다.";
  private static final String MEMBER_NOT_FOUND_MESSAGE = "해당 멤버를 찾을 수 없습니다.";
  private final GatheringRepository gatheringRepository;
  private final MemberRepository memberRepository;

  @Before("@annotation(gatheringLimit)")
  public void enforceGatheringLimit(JoinPoint joinPoint, GatheringLimit gatheringLimit) {
    long currentMemberId = (long) joinPoint.getArgs()[0];
    int maxLimit = gatheringLimit.value();

    Optional<Member> currentMemberOpt = memberRepository.findById(currentMemberId);
    if (currentMemberOpt.isEmpty()) {
      throw new IllegalArgumentException(MEMBER_NOT_FOUND_MESSAGE);
    }

    Member currentMember = currentMemberOpt.get();
    if (currentMember.getMembership() == Membership.FREE &&
        gatheringRepository.countByMemberId(currentMemberId) >= maxLimit) {
      throw new RuntimeException(String.format(EXCEED_LIMIT_MESSAGE,
          gatheringRepository.countByMemberId(currentMemberId)));
    }
  }
}
