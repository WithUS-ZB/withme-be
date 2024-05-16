package com.withus.withmebe.security.domain;

import com.withus.withmebe.member.entity.Member;
import com.withus.withmebe.member.type.Role;
import com.withus.withmebe.security.domain.oauth2.OAuth2UserAttributes;
import jakarta.validation.constraints.NotNull;

public record UserDetailsDomain(
    @NotNull
    Long id,

    String password,

    @NotNull
    Role role,

    OAuth2UserAttributes attributes,

    boolean isMobileAuthenticatedMember,

    boolean isAdditionalInfoRequired

) {

  public static UserDetailsDomain fromEntity(Member member, OAuth2UserAttributes attributes) {
    return new UserDetailsDomain(
        member.getId(),
        member.getPassword(),
        member.getRole(),
        attributes,
        isMobileAuthenticatedMember(member),
        isAdditionalInfoRequired(member)
    );
  }

  private static boolean isMobileAuthenticatedMember(Member member) {
    return member.getPhoneNumber() != null;
  }

  private static boolean isAdditionalInfoRequired(Member member){
    return member.getGender() == null || member.getBirthDate() == null;
  }
}
