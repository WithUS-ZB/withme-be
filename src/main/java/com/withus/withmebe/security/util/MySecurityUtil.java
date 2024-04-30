package com.withus.withmebe.security.util;

import static com.withus.withmebe.common.exception.ExceptionCode.AUTHENTICATION_ISSUE;

import com.withus.withmebe.common.exception.CustomException;
import com.withus.withmebe.security.domain.CustomUserDetails;
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
}
