package com.withus.withmebe.member.dto.member;

import com.withus.withmebe.member.type.Membership;

public record MemberInfoDto(
    String nickName,
    String profileImg,
    Membership membership
) {

}
