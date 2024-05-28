package com.withus.withmebe.gathering.annotation;

import java.lang.annotation.RetentionPolicy;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.*;

@Documented
@Constraint(validatedBy = DateRangeValidator.class)
@Target({TYPE_USE, TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDateRange {
  String message() default "모임 모집기간 시작과 끝 기간이 알맞지 않습니다.";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}