package com.withus.withmebe.member.dto;

import com.withus.withmebe.member.entity.Member;
import com.withus.withmebe.member.type.Membership;

public record MemberSimpleDetailDto(
    String nickName,
    String profileImg,
    Membership membership
) {
  public static MemberSimpleDetailDto fromEntity(Member member){
    return new MemberSimpleDetailDto(
      member.getNickName(),
      member.getProfileImg(),
      member.getMembership()
    );
  }
}
