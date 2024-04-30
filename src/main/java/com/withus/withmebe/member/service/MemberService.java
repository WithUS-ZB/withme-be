package com.withus.withmebe.member.service;

import static com.withus.withmebe.common.exception.ExceptionCode.ENTITY_NOT_FOUND;

import com.withus.withmebe.common.exception.CustomException;
import com.withus.withmebe.member.dto.MemberSimpleDetailDto;
import com.withus.withmebe.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;
  public MemberSimpleDetailDto read(Long userId) {
    return MemberSimpleDetailDto.fromEntity(
      memberRepository.findById(userId)
          .orElseThrow(() -> new CustomException(ENTITY_NOT_FOUND))
    );
  }
}
