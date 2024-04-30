package com.withus.withmebe.security.domain;

import com.withus.withmebe.member.entity.Member;
import com.withus.withmebe.member.type.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserDetailsDomain(
    @NotNull
    Long id,

    @NotBlank
    String password,

    @NotNull
    Role role
) {

  public static UserDetailsDomain fromEntity(Member member) {
    return new UserDetailsDomain(
        member.getId(),
        member.getPassword(),
        member.getRole()
    );
  }
}
