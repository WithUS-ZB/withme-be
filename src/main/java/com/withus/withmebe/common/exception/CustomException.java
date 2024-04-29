package com.withus.withmebe.common.exception;

import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {

  private final HttpStatus httpStatus;

  public CustomException(ExceptionCode exceptionCode) {
    super(exceptionCode.getMessage());
    this.httpStatus = exceptionCode.getStatus();
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }
}
