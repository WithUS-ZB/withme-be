package com.withus.withmebe.security.handler;

import static com.withus.withmebe.common.exception.ExceptionCode.AUTHENTICATION_ISSUE;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.withus.withmebe.common.exception.CustomException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2FailHandler implements AuthenticationFailureHandler {

  private final ObjectMapper objectMapper;

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException exception) throws IOException, ServletException {
    log.info("소셜 로그인 실패");

    response.setContentType(APPLICATION_JSON_VALUE);
    response.setCharacterEncoding(UTF_8.name());
    response.setStatus(BAD_REQUEST.value());

    objectMapper.writeValue(response.getWriter(), new CustomException(AUTHENTICATION_ISSUE));
  }
}
