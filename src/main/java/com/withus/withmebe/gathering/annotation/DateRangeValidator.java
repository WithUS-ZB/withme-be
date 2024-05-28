package com.withus.withmebe.gathering.annotation;

import com.withus.withmebe.gathering.annotation.ValidDateRange;
import com.withus.withmebe.gathering.dto.request.AddGatheringRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, AddGatheringRequest> {

  @Override
  public boolean isValid(AddGatheringRequest request, ConstraintValidatorContext context) {
    LocalDate recruitmentStartDt = request.getRecruitmentStartDt();
    LocalDate recruitmentEndDt = request.getRecruitmentEndDt();
    return !recruitmentStartDt.isAfter(recruitmentEndDt);
  }
}
