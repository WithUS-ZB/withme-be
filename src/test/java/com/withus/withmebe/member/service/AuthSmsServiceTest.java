package com.withus.withmebe.member.service;

import static com.withus.withmebe.common.exception.ExceptionCode.AUTH_CODE_MISMATCH;
import static com.withus.withmebe.common.exception.ExceptionCode.AUTH_SMS_CODE_NOT_FOUND;
import static com.withus.withmebe.common.exception.ExceptionCode.ENTITY_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.withus.withmebe.common.exception.CustomException;
import com.withus.withmebe.common.service.RedisStringService;
import com.withus.withmebe.member.dto.auth.AuthCodeAndSetPhoneNumberRequestDto;
import com.withus.withmebe.member.dto.auth.SendAuthSmsRequestDto;
import com.withus.withmebe.member.dto.auth.SendAuthSmsResponseDto;
import com.withus.withmebe.member.entity.Member;
import com.withus.withmebe.member.repository.MemberRepository;
import com.withus.withmebe.member.type.Role;
import com.withus.withmebe.security.domain.CustomUserDetails;
import com.withus.withmebe.security.domain.UserDetailsDomain;
import java.util.Optional;
import net.nurigo.sdk.NurigoApp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;
import util.WithMockCustomUser;

@ExtendWith(MockitoExtension.class)
@WithMockCustomUser
class AuthSmsServiceTest {
  @Mock
  private RedisStringService redisService;
  @Mock
  private MemberRepository memberRepository;
  @InjectMocks
  private AuthSmsService authSmsService;

  @BeforeEach
  void setUp() {
    initializeMessageService();
    setSenderPhoneNumber();
    setTextContentTemplate();
    setExpirationSeconds();
    setAuthCodeLength();
    setAuthSmsPrefix();
  }

  @Test
  @DisplayName("휴대폰 인증번호 메시지 전송 - 성공")
  void sendAuthSms() {
    // Given
    SendAuthSmsRequestDto requestDto = new SendAuthSmsRequestDto("010-7777-7777");

    SendAuthSmsResponseDto responseDto = authSmsService.sendAuthSms(requestDto);
    // Then
    assertEquals(60, responseDto.expirationSeconds());
  }

  @Test
  @DisplayName("휴대폰 인증번호 확인 후 저장 - 성공")
  void authCodeAndSetPhoneNumber() {
    // Given
    String phoneNumber = "010-1234-5678";
    String authenticationText = "ZRC8U2";
    String expectedAuthCode = "ZRC8U2"; // 예상되는 인증 코드
    AuthCodeAndSetPhoneNumberRequestDto requestDto
        = new AuthCodeAndSetPhoneNumberRequestDto(phoneNumber, authenticationText);
    String key = "AuthSms: " + phoneNumber;
    Long currentMemberId = 2L;

    given(redisService.getValues(key)).willReturn(expectedAuthCode);

    given(memberRepository.findById(currentMemberId)).willReturn(Optional.of(new Member()));

    given(redisService.deleteKey(key)).willReturn(true);
    // When
    Boolean result = authSmsService.authCodeAndSetPhoneNumber(requestDto, currentMemberId);

    // Then
    assertTrue(result); // 예상대로 삭제가 되었으므로 true를 반환할 것으로 예상
    verify(redisService).deleteKey(key); // deleteKey 메소드가 호출되었는지 확인
    verify(memberRepository).findById(anyLong()); // findById 메소드가 호출되었는지 확인
  }

  @Test
  @DisplayName("휴대폰 인증번호 확인 후 저장 - 실패: 인증번호를 발급해주세요.")
  void authCodeAndSetPhoneNumber_AuthSmsCodeNotFound() {
    // Given
    String phoneNumber = "010-1234-5678";
    String authenticationText = "ZRC8U2";
    AuthCodeAndSetPhoneNumberRequestDto requestDto
        = new AuthCodeAndSetPhoneNumberRequestDto(phoneNumber, authenticationText);
    String key = "AuthSms: " + phoneNumber;
    Long currentMemberId = 2L;

    given(redisService.getValues(key)).willReturn(null);

    // When
    CustomException customException = assertThrows(CustomException.class
        ,()-> authSmsService.authCodeAndSetPhoneNumber(requestDto, currentMemberId));

    // Then
    assertEquals(AUTH_SMS_CODE_NOT_FOUND.getMessage(), customException.getMessage());
  }

  @Test
  @DisplayName("휴대폰 인증번호 확인 후 저장 - 실패: 인증번호가 일치하지 않습니다.")
  void authCodeAndSetPhoneNumber_AuthCodeMismatch() {
    // Given
    String phoneNumber = "010-1234-5678";
    String authenticationText = "ZRC8U2";
    String expectedAuthCode = "ZRC8U3"; // 예상되는 인증 코드
    AuthCodeAndSetPhoneNumberRequestDto requestDto
        = new AuthCodeAndSetPhoneNumberRequestDto(phoneNumber, authenticationText);
    String key = "AuthSms: " + phoneNumber;
    Long currentMemberId = 2L;

    given(redisService.getValues(key)).willReturn(expectedAuthCode);

    // When
    CustomException customException = assertThrows(CustomException.class
        ,()-> authSmsService.authCodeAndSetPhoneNumber(requestDto, currentMemberId));

    // Then
    assertEquals(AUTH_CODE_MISMATCH.getMessage(), customException.getMessage());
  }

  @Test
  @DisplayName("휴대폰 인증번호 확인 후 저장 - 성공")
  void authCodeAndSetPhoneNumber_EntityNotFound() {
    // Given
    String phoneNumber = "010-1234-5678";
    String authenticationText = "ZRC8U2";
    String expectedAuthCode = "ZRC8U2"; // 예상되는 인증 코드
    AuthCodeAndSetPhoneNumberRequestDto requestDto
        = new AuthCodeAndSetPhoneNumberRequestDto(phoneNumber, authenticationText);
    String key = "AuthSms: " + phoneNumber;
    Long currentMemberId = 2L;

    given(redisService.getValues(key)).willReturn(expectedAuthCode);

    given(memberRepository.findById(currentMemberId)).willReturn(Optional.empty());

    // When
    CustomException customException = assertThrows(CustomException.class
        ,()-> authSmsService.authCodeAndSetPhoneNumber(requestDto, currentMemberId));

    // Then
    assertEquals(ENTITY_NOT_FOUND.getMessage(), customException.getMessage());
  }



  private void initializeMessageService() {
    String apiKey = "NCSXQ8ZDMCPA3OE5";
    String apiSecretKey = "OHHDZBMNXNBS07WZ70UYNFWYSEGCURMI";
    String domain = "https://api.coolsms.co.kr";
    ReflectionTestUtils.setField(authSmsService, "messageService",
        NurigoApp.INSTANCE.initialize(apiKey, apiSecretKey, domain));
  }

  private void setSenderPhoneNumber() {
    ReflectionTestUtils.setField(authSmsService, "senderPhoneNumber", "01097799391");
  }

  private void setTextContentTemplate() {
    ReflectionTestUtils.setField(authSmsService, "textContentTemplate",
        "[with me] 인증 문자는 [%s] 입니다.");
  }

  private void setExpirationSeconds() {
    ReflectionTestUtils.setField(authSmsService, "expirationSeconds", 60);
  }

  private void setAuthCodeLength() {
    ReflectionTestUtils.setField(authSmsService, "authCodeLength", 6);
  }

  private void setAuthSmsPrefix() {
    ReflectionTestUtils.setField(authSmsService, "authSmsPrefix", "AuthSms: ");
  }

}