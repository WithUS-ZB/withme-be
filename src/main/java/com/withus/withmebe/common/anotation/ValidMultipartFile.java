package com.withus.withmebe.common.anotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = MultipartFileValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidMultipartFile {
  String message() default "Invalid file";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}