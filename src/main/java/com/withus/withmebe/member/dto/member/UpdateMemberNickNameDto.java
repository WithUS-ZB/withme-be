package com.withus.withmebe.member.dto.member;

import com.withus.withmebe.member.entity.Member;
import jakarta.validation.constraints.NotBlank;

public record UpdateMemberNickNameDto() {

  public record Request(
      @NotBlank
      String nickName
  ) {

  }

  public record Response(
      String nickName
  ) {

    public static Response fromEntity(Member currentMember) {
      return new Response(
          currentMember.getNickName());
    }
  }
}
