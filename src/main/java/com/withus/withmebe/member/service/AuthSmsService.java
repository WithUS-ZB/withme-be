package com.withus.withmebe.member.service;

import com.withus.withmebe.common.service.RedisStringService;
import com.withus.withmebe.member.dto.auth.SendAuthSmsRequestDto;
import com.withus.withmebe.member.dto.auth.SendAuthSmsResponseDto;
import java.security.SecureRandom;
import java.time.Duration;
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

  @Value("${cool-sms.sender-phone-number}")
  private String senderPhoneNumber;

  @Value("${auth-sms-service.text-content-template}")
  private String textContentTemplate;

  @Value("${auth-sms-service.expiration-seconds}")
  private int expirationSeconds;

  @Value("${auth-sms-service.auth-code-length}")
  private int authCodeLength;

  @Value("${redis.key.prefix.auth-sms}")
  private String authSmsPrefix;

  @Transactional
  public SendAuthSmsResponseDto sendAuthSms(SendAuthSmsRequestDto request) {
    String authCode = generateAuthCode(authCodeLength);
    redisService.setValues(authSmsPrefix+request.receiverPhoneNumber(), authCode
        , Duration.ofSeconds(expirationSeconds));
    this.messageService.sendOne(
        request.toMessage(textContentTemplate, senderPhoneNumber, authCode));
    return new SendAuthSmsResponseDto(expirationSeconds, authCode);
  }



  private String generateAuthCode(int len) {
    final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    SecureRandom random = new SecureRandom();
    StringBuilder code = new StringBuilder(len);

    for (int i = 0; i < len; i++) {
      int randomIndex = random.nextInt(CHARACTERS.length());
      char randomChar = CHARACTERS.charAt(randomIndex);
      code.append(randomChar);
    }

    return code.toString();
  }
}
