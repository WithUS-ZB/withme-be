package com.withus.withmebe.member.dto.auth.request;

import com.withus.withmebe.member.annotation.ValidPhoneNumber;
import jakarta.validation.constraints.NotBlank;

public record AuthCodeAndSetPhoneNumberRequestDto(
    @NotBlank
    @ValidPhoneNumber
    String phoneNumber,
    String authenticationText
) {
}
