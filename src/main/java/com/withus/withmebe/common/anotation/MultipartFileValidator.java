package com.withus.withmebe.common.anotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class MultipartFileValidator implements
    ConstraintValidator<ValidMultipartFile, MultipartFile> {

  @Override
  public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
    if (value == null || value.isEmpty()) {
      setMessage(context, "File must not be empty");
      return false;
    }

    String contentType = value.getContentType();
    if (contentType == null || !contentType.startsWith("image/")) {
      setMessage(context, "File must be an image");
      return false;
    }

    if (value.getSize() > 5 * 1024 * 1024) { // 5MB 제한
      setMessage(context, "File must be less than 5MB");
      return false;
    }

    return true;
  }

  private static void setMessage(ConstraintValidatorContext context,
      String fileMustNotBeEmpty) {
    context.disableDefaultConstraintViolation();
    context.buildConstraintViolationWithTemplate(fileMustNotBeEmpty)
        .addConstraintViolation();
  }
}