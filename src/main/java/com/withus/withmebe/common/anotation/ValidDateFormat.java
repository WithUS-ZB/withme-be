package com.withus.withmebe.common.anotation;

import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.format.annotation.DateTimeFormat;


@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@DateTimeFormat(pattern = "yyyy-MM-dd")
public @interface ValidDateFormat {

  String message() default "올바른 Date 형식이 아닙니다.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}