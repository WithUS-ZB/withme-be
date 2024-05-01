package com.withus.withmebe.member.service;

import static com.withus.withmebe.common.exception.ExceptionCode.ENTITY_NOT_FOUND;
import static com.withus.withmebe.common.exception.ExceptionCode.NICKNAME_CONFLICT;

import com.withus.withmebe.common.exception.CustomException;
import com.withus.withmebe.member.dto.MemberDetailDto;
import com.withus.withmebe.member.dto.MemberSimpleDetailDto;
import com.withus.withmebe.member.entity.Member;
import com.withus.withmebe.member.repository.MemberRepository;
import com.withus.withmebe.security.util.MySecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;
  @Transactional(readOnly = true)
  public MemberSimpleDetailDto read(Long memberId) {
    return MemberSimpleDetailDto.fromEntity(
        getMemberById(memberId)
    );
  }

  @Transactional(readOnly = true)
  public MemberDetailDto readCurretLoginMemberDetail() {
    return MemberDetailDto.fromEntity(
        getCurrentMember()
    );
  }

  @Transactional(readOnly = true)
  public Boolean existsEmail(String email) {
    return memberRepository.existsByEmail(email);
  }

  @Transactional(readOnly = true)
  public Boolean existsNickname(String nickname) {
    return memberRepository.existsByNickName(nickname);
  }

  @Transactional
  public MemberDetailDto updateProfileImg(String profileImg) {
    Member currentMember = getCurrentMember();
    currentMember.setProfileImg(profileImg);
    return MemberDetailDto.fromEntity(currentMember);
  }

  @Transactional
  public MemberDetailDto updateNickname(String nickname) {
    Member currentMember = getCurrentMember();
    if(memberRepository.existsByNickName(nickname)){
      throw new CustomException(NICKNAME_CONFLICT);
    }
    currentMember.setProfileImg(nickname);
    return MemberDetailDto.fromEntity(currentMember);
  }

  private Member getMemberById(Long id) {
    return memberRepository.findById(id)
        .orElseThrow(() -> new CustomException(ENTITY_NOT_FOUND));
  }

  private Member getCurrentMember(){
    return getMemberById(MySecurityUtil.getCurrentLoginMemberId());
  }
}
