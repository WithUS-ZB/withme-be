package com.withus.withmebe.member.service;

import static com.withus.withmebe.common.exception.ExceptionCode.ENTITY_NOT_FOUND;

import com.withus.withmebe.common.exception.CustomException;
import com.withus.withmebe.member.dto.MemberDetailDto;
import com.withus.withmebe.member.dto.MemberSimpleDetailDto;
import com.withus.withmebe.member.entity.Member;
import com.withus.withmebe.member.repository.MemberRepository;
import com.withus.withmebe.security.util.MySecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;
  public MemberSimpleDetailDto read(Long memberId) {
    return MemberSimpleDetailDto.fromEntity(
        getMemberById(memberId)
    );
  }

  public MemberDetailDto readCurretLoginMemberDetail() {
    return MemberDetailDto.fromEntity(
        getMemberById(MySecurityUtil.getCurrentLoginMemberId())
    );
  }

  private Member getMemberById(Long id) {
    return memberRepository.findById(id)
        .orElseThrow(() -> new CustomException(ENTITY_NOT_FOUND));
  }
}
