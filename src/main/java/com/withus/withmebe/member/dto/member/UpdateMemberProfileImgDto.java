package com.withus.withmebe.member.dto.member;

import com.withus.withmebe.common.anotation.ValidMultipartFile;
import com.withus.withmebe.member.entity.Member;
import org.springframework.web.multipart.MultipartFile;

public record UpdateMemberProfileImgDto() {

  public record Request(
      @ValidMultipartFile
      MultipartFile profileImg
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
