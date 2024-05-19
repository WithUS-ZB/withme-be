package com.withus.withmebe.member.service;

import static com.withus.withmebe.common.exception.ExceptionCode.EMAIL_CONFLICT;
import static com.withus.withmebe.common.exception.ExceptionCode.ENTITY_NOT_FOUND;
import static com.withus.withmebe.common.exception.ExceptionCode.PASSWORD_CHK_MISMATCH;
import static com.withus.withmebe.common.exception.ExceptionCode.PASSWORD_MISMATCH;
import static com.withus.withmebe.member.type.Gender.MALE;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.withus.withmebe.common.exception.CustomException;
import com.withus.withmebe.member.dto.auth.SigninDto;
import com.withus.withmebe.member.dto.auth.SignupDto;
import com.withus.withmebe.member.entity.Member;
import com.withus.withmebe.member.repository.MemberRepository;
import com.withus.withmebe.security.jwt.provider.TokenProvider;
import com.withus.withmebe.security.jwt.repository.AccessTokenRepository;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import util.security.WithMockCustomUser;

@ExtendWith(MockitoExtension.class)
@WithMockCustomUser
class AuthServiceTest {

  @Mock
  private MemberRepository memberRepository;
  @Mock
  private PasswordEncoder passwordEncoder;
  @Mock
  private TokenProvider tokenProvider;

  @Mock
  private AccessTokenRepository accessTokenRepository;
  @InjectMocks
  private AuthService authService;
  private String email = "test@example.com";
  private String password = "password";
  private String encodedPassword = "encodedPassword";
  private String accessToken = "accessToken";
  private String wrongPassword = "wrongPassword";
  private LocalDate birthDate = LocalDate.of(2020, 1, 1);

  @Test
  void signup() {
    // Given
    SignupDto.Request request = new SignupDto.Request(email, password, password,
        birthDate, MALE);
    when(memberRepository.existsByEmail(request.email())).thenReturn(false);
    when(passwordEncoder.encode(request.password())).thenReturn(encodedPassword);
    Member expectedMember = request.toEntity(encodedPassword);
    when(memberRepository.save(argThat(member ->
        member.getEmail().equals(expectedMember.getEmail()) &&
            member.getPassword().equals(expectedMember.getPassword()) &&
            member.getGender() == expectedMember.getGender() &&
            member.getBirthDate().equals(expectedMember.getBirthDate())
    ))).thenReturn(expectedMember);
    // When
    SignupDto.Response response = authService.signup(request);

    // Then
    assertNotNull(response);
    verify(memberRepository, times(1)).existsByEmail(request.email());
    verify(passwordEncoder, times(1)).encode(request.password());
    verify(memberRepository, times(1)).save(argThat(member ->
        member.getEmail().equals(expectedMember.getEmail()) &&
            member.getPassword().equals(expectedMember.getPassword()) &&
            member.getGender() == expectedMember.getGender() &&
            member.getBirthDate().equals(expectedMember.getBirthDate())
    ));
  }

  @Test
  void signup_passwordMismatch() {
    // Given
    SignupDto.Request request = new SignupDto.Request(email, password, wrongPassword,
        birthDate, MALE);

    // When & Then
    CustomException customException = assertThrows(CustomException.class,
        () -> authService.signup(request));
    assertEquals(PASSWORD_CHK_MISMATCH.getMessage(), customException.getMessage());
  }

  @Test
  void signup_emailConflict() {
    // Given
    SignupDto.Request request = new SignupDto.Request(email, password, password,
        birthDate, MALE);
    when(memberRepository.existsByEmail(email)).thenReturn(true);

    // When & Then
    CustomException customException = assertThrows(CustomException.class,
        () -> authService.signup(request));
    assertEquals(EMAIL_CONFLICT.getMessage(), customException.getMessage());
  }

  @Test
  void signin() {
    // Given
    SigninDto.Request request = new SigninDto.Request(email, password);
    Member member = createEntity();

    when(memberRepository.findByEmail(email)).thenReturn(Optional.of(member));
    when(passwordEncoder.matches(request.password(), member.getPassword())).thenReturn(true);
    when(tokenProvider.generateToken(member.getId().toString(),
        List.of(member.getRole().name()))).thenReturn(accessToken);
    when(tokenProvider.getTokenDuration(accessToken)).thenReturn(Duration.ofMinutes(60));
    // When
    SigninDto.Response response = authService.signin(request);

    // Then
    assertNotNull(response);
    assertEquals(accessToken, response.accessToken());
    verify(memberRepository, times(1)).findByEmail(email);
    verify(passwordEncoder, times(1)).matches(request.password(), member.getPassword());
    verify(tokenProvider, times(1)).generateToken(member.getId().toString(),
        List.of(member.getRole().name()));
  }

  @Test
  void signin_entityNotFound() {
    // Given
    SigninDto.Request request = new SigninDto.Request(email, password);
    when(memberRepository.findByEmail(email)).thenReturn(Optional.empty());

    // When & Then
    CustomException customException = assertThrows(CustomException.class,
        () -> authService.signin(request));
    assertEquals(ENTITY_NOT_FOUND.getMessage(), customException.getMessage());
  }

  @Test
  void signin_passwordMismatch() {
    // Given
    SigninDto.Request request = new SigninDto.Request(email, wrongPassword);
    Member member = createEntity();

    when(memberRepository.findByEmail(email)).thenReturn(Optional.of(member));
    when(passwordEncoder.matches(request.password(), member.getPassword())).thenReturn(false);

    // When & Then
    CustomException customException = assertThrows(CustomException.class,
        () -> authService.signin(request));
    assertEquals(PASSWORD_MISMATCH.getMessage(), customException.getMessage());
  }

  @Test
  void logout() {
    // Given
    Long currentMemberId = 1L;

    // When
    authService.logout(currentMemberId);

    // Then
    verify(accessTokenRepository, times(1)).delete(currentMemberId);
  }

  private Member createEntity() {
    Member member = Member.builder()
        .email(email)
        .password(encodedPassword)
        .birthDate(birthDate)
        .gender(MALE)
        .signupDttm(LocalDateTime.now())
        .build();
    ReflectionTestUtils.setField(member, "id", 1L);

    return member;
  }
}