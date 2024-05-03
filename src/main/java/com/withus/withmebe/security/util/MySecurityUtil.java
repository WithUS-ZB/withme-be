package com.withus.withmebe.security.util;

import static com.withus.withmebe.common.exception.ExceptionCode.AUTHENTICATION_ISSUE;

import com.withus.withmebe.common.exception.CustomException;
import com.withus.withmebe.security.domain.CustomUserDetails;
import java.security.SecureRandom;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.context.SecurityContextHolder;

@UtilityClass
public class MySecurityUtil {

  public static CustomUserDetails getCustomUserDetails() {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (!(principal instanceof CustomUserDetails)) {
      throw new CustomException(AUTHENTICATION_ISSUE);
    }
    return (CustomUserDetails) principal;
  }

  public static Long getCurrentLoginMemberId(){
    return getCustomUserDetails().getMemberId();
  }

  public static String generateAuthCode() {
    final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    final int CODE_LENGTH = 6;

    SecureRandom random = new SecureRandom();
    StringBuilder code = new StringBuilder(CODE_LENGTH);

    for (int i = 0; i < CODE_LENGTH; i++) {
      int randomIndex = random.nextInt(CHARACTERS.length());
      char randomChar = CHARACTERS.charAt(randomIndex);
      code.append(randomChar);
    }

    return code.toString();
  }
}
