package com.withus.withmebe.member.dto.member.request;

import com.withus.withmebe.common.anotation.ValidDateFormat;
import com.withus.withmebe.member.type.Gender;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record AdditionalInfoRequestDto (
    @ValidDateFormat
    @NotNull
    LocalDate birthDate,
    @NotNull
    Gender gender
) {
}
