package com.withus.withmebe.member.service;

import static com.withus.withmebe.common.exception.ExceptionCode.EMAIL_CONFLICT;
import static com.withus.withmebe.common.exception.ExceptionCode.ENTITY_NOT_FOUND;
import static com.withus.withmebe.common.exception.ExceptionCode.PASSWORD_CHK_MISMATCH;
import static com.withus.withmebe.common.exception.ExceptionCode.PASSWORD_MISMATCH;

import com.withus.withmebe.common.exception.CustomException;
import com.withus.withmebe.member.dto.SigninDto;
import com.withus.withmebe.member.dto.SignupDto;
import com.withus.withmebe.member.entity.Member;
import com.withus.withmebe.member.repository.MemberRepository;
import com.withus.withmebe.security.jwt.provider.TokenProvider;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;
  private final TokenProvider tokenProvider;

  @Transactional
  public SignupDto.Response signup(SignupDto.Request request) {
    if (!request.password().equals(request.passwordChk())){
      throw new CustomException(PASSWORD_CHK_MISMATCH);
    }
    if (memberRepository.existsByEmail(request.email())) {
      throw new CustomException(EMAIL_CONFLICT);
    }

    return SignupDto.Response.fromEntity(
        memberRepository.save(
            request.toEntity(passwordEncoder.encode(request.password()), getUniqueNickName())));
  }

  public SigninDto.Response signin(SigninDto.Request request) {
    Member member = memberRepository.findByEmail(request.email())
        .orElseThrow(() -> new CustomException(ENTITY_NOT_FOUND));

    if (!passwordEncoder.matches(request.password(), member.getPassword())) {
      throw new CustomException(PASSWORD_MISMATCH);
    }

    return new SigninDto.Response(
        tokenProvider.generateToken(member.getId().toString(), List.of(member.getRole().name())));
  }

  private String getUniqueNickName(){
    UUID uuid;
    do{
      uuid = UUID.randomUUID();
    }while (memberRepository.existsByNickName(uuid.toString()));
    return uuid.toString();
  }
}
