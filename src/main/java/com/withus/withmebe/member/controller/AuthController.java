package com.withus.withmebe.member.controller;

import static org.springframework.http.HttpStatus.OK;

import com.withus.withmebe.member.dto.auth.SigninDto;
import com.withus.withmebe.member.dto.auth.SigninDto.Response;
import com.withus.withmebe.member.dto.auth.SignupDto;
import com.withus.withmebe.member.service.AuthService;
import com.withus.withmebe.security.anotation.CurrentMemberId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthService authService;

  @PostMapping("/signup")
  public ResponseEntity<SignupDto.Response> signup(@Valid @RequestBody SignupDto.Request request) {
    return ResponseEntity.ok(authService.signup(request));
  }

  @PostMapping("/signin")
  public ResponseEntity<Void> signin(@Valid @RequestBody SigninDto.Request request) {
    MultiValueMap<String, String> header = new HttpHeaders();
    Response response = authService.signin(request);
    header.add("Authorization", "Bearer " + response.accessToken());
    return new ResponseEntity<>(header, OK);
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(@CurrentMemberId Long currentMemberId){
    authService.logout(currentMemberId);
    return ResponseEntity.ok().build();
  }

}