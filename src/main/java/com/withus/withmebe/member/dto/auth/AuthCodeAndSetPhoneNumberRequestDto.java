package com.withus.withmebe.member.dto.auth;

import com.withus.withmebe.member.annotation.ValidPhoneNumber;
import jakarta.validation.constraints.NotBlank;

public record AuthCodeAndSetPhoneNumberRequestDto(
    @NotBlank
    @ValidPhoneNumber
    String phoneNumber,
    String authenticationText
) {
}
