package com.withus.withmebe.security.domain.oauth2;

import static com.withus.withmebe.common.exception.ExceptionCode.AUTHENTICATION_ISSUE;
import static com.withus.withmebe.member.type.SignupPath.KAKAO;

import com.withus.withmebe.common.exception.CustomException;
import lombok.experimental.UtilityClass;
import org.springframework.security.oauth2.core.user.OAuth2User;

@UtilityClass
public class OAuth2UserAttributesCreater {

  public static OAuth2UserAttributes create(
      String registrationId, OAuth2User oAuth2User, String userNameAttributeName) {
    if (registrationId.equals(KAKAO.getValue())) {
      return new KakaoUserAttributes(oAuth2User, userNameAttributeName);
    } else {
      throw new CustomException(AUTHENTICATION_ISSUE);
    }
  }
}
