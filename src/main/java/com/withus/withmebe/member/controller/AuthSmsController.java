package com.withus.withmebe.member.controller;

import com.withus.withmebe.member.dto.auth.request.AuthCodeAndSetPhoneNumberRequestDto;
import com.withus.withmebe.member.dto.auth.request.SendAuthSmsRequestDto;
import com.withus.withmebe.member.dto.auth.response.SendAuthSmsResponseDto;
import com.withus.withmebe.member.service.AuthSmsService;
import com.withus.withmebe.security.anotation.CurrentMemberId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

  @PutMapping
  public ResponseEntity<Boolean> authCodeAndSetPhoneNumber(
    @Valid @RequestBody AuthCodeAndSetPhoneNumberRequestDto request
      , @CurrentMemberId Long currentMemberId
  ){
    return ResponseEntity.ok(authSmsService.authCodeAndSetPhoneNumber(request, currentMemberId));
  }
}
