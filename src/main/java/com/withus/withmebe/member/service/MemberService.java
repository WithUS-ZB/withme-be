package com.withus.withmebe.member.service;

import static com.withus.withmebe.common.exception.ExceptionCode.ENTITY_NOT_FOUND;
import static com.withus.withmebe.common.exception.ExceptionCode.NICKNAME_CONFLICT;

import com.withus.withmebe.common.exception.CustomException;
import com.withus.withmebe.member.dto.member.MemberDetailDto;
import com.withus.withmebe.member.dto.member.MemberInfoDto;
import com.withus.withmebe.member.dto.member.UpdateMemberNickNameDto;
import com.withus.withmebe.member.dto.member.UpdateMemberProfileImgDto;
import com.withus.withmebe.member.entity.Member;
import com.withus.withmebe.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;
  @Transactional(readOnly = true)
  public MemberInfoDto read(Long memberId) {
    return MemberInfoDto.fromEntity(
        getMemberById(memberId)
    );
  }

  @Transactional(readOnly = true)
  public MemberDetailDto readCurretLoginMemberDetail(Long currentMemberId) {
    return MemberDetailDto.fromEntity(
        getMemberById(currentMemberId)
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
  public UpdateMemberProfileImgDto.Response updateProfileImg(
      UpdateMemberProfileImgDto.Request request, Long currentMemberId) {
    Member currentMember = getMemberById(currentMemberId);
    // TODO: S3로 이미지 처리 필요
    currentMember.setProfileImg(request.profileImg());
    return UpdateMemberProfileImgDto.Response.fromEntity(currentMember);
  }

  @Transactional
  public UpdateMemberNickNameDto.Response updateNickname(
      UpdateMemberNickNameDto.Request request, Long currentMemberId) {
    String requestNickName = request.nickName();
    if(memberRepository.existsByNickName(requestNickName)){
      throw new CustomException(NICKNAME_CONFLICT);
    }
    Member currentMember = getMemberById(currentMemberId);
    currentMember.setNickName(requestNickName);
    return UpdateMemberNickNameDto.Response.fromEntity(currentMember);
  }

  private Member getMemberById(Long id) {
    return memberRepository.findById(id)
        .orElseThrow(() -> new CustomException(ENTITY_NOT_FOUND));
  }
}
