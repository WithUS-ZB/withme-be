package com.withus.withmebe.member.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record SigninDto() {
  public record Request(
      @NotBlank
      String email,
      @NotBlank
      String password
  ) {

  }

  public record Response(
      String accessToken
  ) {

  }
}
