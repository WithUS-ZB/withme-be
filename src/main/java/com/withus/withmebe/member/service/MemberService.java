package com.withus.withmebe.member.service;

import static com.withus.withmebe.common.exception.ExceptionCode.ENTITY_NOT_FOUND;
import static com.withus.withmebe.common.exception.ExceptionCode.NICKNAME_CONFLICT;

import com.withus.withmebe.common.exception.CustomException;
import com.withus.withmebe.member.dto.member.MemberDetailDto;
import com.withus.withmebe.member.dto.member.MemberInfoDto;
import com.withus.withmebe.member.dto.member.UpdateMemberNickNameDto;
import com.withus.withmebe.member.dto.member.UpdateMemberProfileImgDto;
import com.withus.withmebe.member.dto.member.request.AdditionalInfoRequestDto;
import com.withus.withmebe.member.entity.Member;
import com.withus.withmebe.member.repository.MemberRepository;
import com.withus.withmebe.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;
  private final S3Service s3Service;
  @Transactional(readOnly = true)
  public MemberInfoDto read(Long memberId) {
    return getMemberById(memberId).toMemberInfoDto();
  }

  @Transactional(readOnly = true)
  public MemberDetailDto readCurrentLoginMemberDetail(Long currentMemberId) {
    return getMemberById(currentMemberId).toMemberDetailDto();
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

    currentMember.setProfileImg(s3Service.uploadFile(request.profileImg()).url());
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

  @Transactional
  public MemberDetailDto updateAdditionalInfo(AdditionalInfoRequestDto request, Long currentMemberId) {
    return getMemberById(currentMemberId).updateAdditionalInfo(request).toMemberDetailDto();
  }

  private Member getMemberById(Long id) {
    return memberRepository.findById(id)
        .orElseThrow(() -> new CustomException(ENTITY_NOT_FOUND));
  }
}
