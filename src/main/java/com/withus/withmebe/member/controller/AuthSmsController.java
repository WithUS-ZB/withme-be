package com.withus.withmebe.member.controller;

import com.withus.withmebe.member.dto.auth.SendAuthSmsRequestDto;
import com.withus.withmebe.member.dto.auth.SendAuthSmsResponseDto;
import com.withus.withmebe.member.service.AuthSmsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/sms")
@RequiredArgsConstructor
public class AuthSmsController {

  private final AuthSmsService authSmsService;

  @PostMapping
  public ResponseEntity<SendAuthSmsResponseDto> sendAuthSms(
      @Valid @RequestBody SendAuthSmsRequestDto request) {
    return ResponseEntity.ok(authSmsService.sendAuthSms(request));
  }
}
