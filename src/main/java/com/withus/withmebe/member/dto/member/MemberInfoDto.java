package com.withus.withmebe.member.dto.member;

import com.withus.withmebe.member.entity.Member;
import com.withus.withmebe.member.type.Membership;

public record MemberInfoDto(
    String nickName,
    String profileImg,
    Membership membership
) {
  public static MemberInfoDto fromEntity(Member member){
    return new MemberInfoDto(
      member.getNickName(),
      member.getProfileImg(),
      member.getMembership()
    );
  }
}
