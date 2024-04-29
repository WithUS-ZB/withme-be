package com.withus.withmebe.member.service;

import static com.withus.withmebe.common.exception.ExceptionCode.EMAIL_CONFLICT;

import com.withus.withmebe.common.exception.CustomException;
import com.withus.withmebe.member.dto.SignupDto;
import com.withus.withmebe.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;

  public SignupDto.Response signup(SignupDto.Request request) {
    if (memberRepository.existsByEmail(request.email())) {
      throw new CustomException(EMAIL_CONFLICT);
    }

    return SignupDto.Response.fromEntity(memberRepository.save(request.toEntity(passwordEncoder)));
  }


}
