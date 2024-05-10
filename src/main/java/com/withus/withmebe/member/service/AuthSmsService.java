package com.withus.withmebe.member.service;

import static com.withus.withmebe.common.exception.ExceptionCode.AUTH_CODE_MISMATCH;
import static com.withus.withmebe.common.exception.ExceptionCode.AUTH_SMS_CODE_NOT_FOUND;
import static com.withus.withmebe.common.exception.ExceptionCode.ENTITY_NOT_FOUND;

import com.withus.withmebe.common.exception.CustomException;
import com.withus.withmebe.common.service.RedisStringService;
import com.withus.withmebe.member.dto.auth.request.AuthCodeAndSetPhoneNumberRequestDto;
import com.withus.withmebe.member.dto.auth.request.SendAuthSmsRequestDto;
import com.withus.withmebe.member.dto.auth.response.SendAuthSmsResponseDto;
import com.withus.withmebe.member.repository.MemberRepository;
import java.time.Duration;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthSmsService {

  private final DefaultMessageService messageService;
  private final RedisStringService redisService;
  private final MemberRepository memberRepository;

  @Value("${cool-sms.sender-phone-number}")
  private String senderPhoneNumber;

  private static final String TEXT_CONTENT_TEMPLATE = "[with me] 인증 문자는 [%s] 입니다.";

  private static final int EXPIRATION_SECONDS = 60;

  @Value("${redis.key.prefix.auth-sms}")
  private String authSmsPrefix;

  @Transactional
  public SendAuthSmsResponseDto sendAuthSms(SendAuthSmsRequestDto request) {
    String authCode = generateAuthCode();
    redisService.setValues(authSmsPrefix+request.receiverPhoneNumber(), authCode
        , Duration.ofSeconds(EXPIRATION_SECONDS));
    this.messageService.sendOne(
        request.toMessage(TEXT_CONTENT_TEMPLATE, senderPhoneNumber, authCode));
    return new SendAuthSmsResponseDto(EXPIRATION_SECONDS, authCode);
  }

  private String generateAuthCode() {
    Random random = new Random();
    int randomNumber = random.nextInt(1000000);
    return String.format("%06d", randomNumber);
  }

  @Transactional
  public Boolean authCodeAndSetPhoneNumber(
      AuthCodeAndSetPhoneNumberRequestDto request, Long currentMemberId) {
    String key = authSmsPrefix + request.phoneNumber();
    String values = redisService.getValues(key);
    if(values == null){
      throw new CustomException(AUTH_SMS_CODE_NOT_FOUND);
    }
    if(!values.equals(request.authenticationText())){
      throw new CustomException(AUTH_CODE_MISMATCH);
    }
    memberRepository.findById(currentMemberId)
        .orElseThrow(() -> new CustomException(ENTITY_NOT_FOUND))
        .setPhoneNumber(request.phoneNumber());
    return redisService.deleteKey(key);
  }
}
