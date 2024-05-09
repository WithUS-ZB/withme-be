package com.withus.withmebe.member.dto.member;

import com.withus.withmebe.member.entity.Member;
import jakarta.validation.constraints.NotBlank;

public record UpdateMemberProfileImgDto() {

  public record Request(
      @NotBlank
      String profileImg
  ) {

  }

  public record Response(
      String nickName,
      String profileImg
  ) {

    public static Response fromEntity(Member currentMember) {
      return new Response(
          currentMember.getNickName()
          , currentMember.getProfileImg());
    }
  }
}
