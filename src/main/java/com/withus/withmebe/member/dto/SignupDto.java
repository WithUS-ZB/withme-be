package com.withus.withmebe.member.dto;

import com.withus.withmebe.member.entity.Member;
import com.withus.withmebe.member.type.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.password.PasswordEncoder;

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
      @DateTimeFormat(pattern = "yyyy-MM-dd")
      LocalDate birthDate,

      @NotNull
      Gender gender
  ) {

    public Member toEntity(PasswordEncoder passwordEncoder) {
      return Member.builder()
          .email(this.email)
          .password(passwordEncoder.encode(this.password))
          .birthDate(this.birthDate)
          .gender(this.gender)
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
