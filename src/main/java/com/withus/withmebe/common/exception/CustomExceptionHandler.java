package com.withus.withmebe.common.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

  @ExceptionHandler(Exception.class)
  @ResponseStatus(BAD_REQUEST)
  public ResponseEntity<Map<String, String>> handleException(
      Exception e, HttpServletRequest request) {
    HttpStatus status = BAD_REQUEST;
    log.error("ExceptionHandler 호출:{}:{}({}) {}"
        , e.getClass().getSimpleName(), status.value(), request.getRequestURI(), e.getMessage());
    return ResponseEntity.status(status).body(getResponse(e, status));

  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<Map<String, String>> handleException(
      RuntimeException e, HttpServletRequest request) {

    HttpStatus status = BAD_REQUEST;
    if (e instanceof CustomException customException) {
      status = customException.getHttpStatus();
    }
    log.error("RuntimeExceptionHandler 호출:{}:{}({}) {}"
        , e.getClass().getSimpleName(), status.value(), request.getRequestURI(), e.getMessage());

    return ResponseEntity.status(status).body(getResponse(e, status));

  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(BAD_REQUEST)
  public ResponseEntity<Map<String, String>> handleMethodArgumentNotValid(
      MethodArgumentNotValidException e,
      HttpServletRequest request) {
    HttpStatus status = BAD_REQUEST;
    Map<String, String> errors = getResponse(e, status);
    for (FieldError error : e.getBindingResult().getFieldErrors()) {
      errors.put(error.getField(), error.getDefaultMessage());
    }
    log.error("MethodArgumentNotValidExceptionHandler 호출:{}:{}({}) {}"
        , e.getClass().getSimpleName(), status.value(), request.getRequestURI(),
        e.getMessage());
    return ResponseEntity.badRequest().body(errors);
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  @ResponseStatus(BAD_REQUEST)
  public ResponseEntity<Map<String, String>> handleMissingServletRequestParameter(
      MissingServletRequestParameterException e,
      HttpServletRequest request) {
    HttpStatus status = BAD_REQUEST;
    log.error("MissingServletRequestParameterException 호출:{}:{}({}) {}"
        , e.getClass().getSimpleName(), status.value(),
        request.getRequestURI() + "?" + request.getQueryString(), e.getMessage());
    return ResponseEntity.status(status).body(getResponse(e, status));
  }

  private static Map<String, String> getResponse(Exception e, HttpStatus status) {
    Map<String, String> response = new HashMap<>();
    response.put("error type", status.getReasonPhrase());
    response.put("code", String.valueOf(status.value()));
    response.put("message", e.getMessage());
    return response;
  }
}
