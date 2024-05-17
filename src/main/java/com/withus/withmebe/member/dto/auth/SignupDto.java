package com.withus.withmebe.member.dto.auth;

import com.withus.withmebe.common.anotation.ValidDateFormat;
import com.withus.withmebe.member.entity.Member;
import com.withus.withmebe.member.type.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record SignupDto() {
  public record Request(
      @NotBlank
      @Email
      String email,
      @NotBlank
      String password,
      @NotBlank
      String passwordChk,

      @NotNull
      @ValidDateFormat
      LocalDate birthDate,

      @NotNull
      Gender gender
  ) {

    public Member toEntity(String encodedPassword) {
      return Member.builder()
          .email(this.email)
          .password(encodedPassword)
          .nickName(this.email)
          .birthDate(this.birthDate)
          .gender(this.gender)
          .signupDttm(LocalDateTime.now())
          .build();
    }
  }

  public record Response(
      Long id,
      String email,
      String password,
      LocalDate birthDate,
      Gender gender
  ) {

    public static Response fromEntity(Member member) {
      return new Response(
          member.getId(),
          member.getEmail(),
          member.getPassword(),
          member.getBirthDate(),
          member.getGender());
    }
  }
}
