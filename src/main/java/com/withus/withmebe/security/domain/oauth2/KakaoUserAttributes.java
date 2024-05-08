package com.withus.withmebe.security.domain.oauth2;

import static com.withus.withmebe.member.type.SignupPath.KAKAO;

import com.withus.withmebe.member.type.SignupPath;
import java.util.LinkedHashMap;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class KakaoUserAttributes extends OAuth2UserAttributes {


  public KakaoUserAttributes(OAuth2User oAuth2User, String userNameAttributeName) {
    super(oAuth2User, userNameAttributeName);
  }

  @Override
  protected Object getNickNameFromOAuth2User() {
    return ((LinkedHashMap) oAuth2User.getAttributes().get("properties")).get("nickname");
  }

  @Override
  protected Object getProfileImageFromOAuth2User() {
    return ((LinkedHashMap) oAuth2User.getAttributes().get("properties")).get("profile_image");
  }

  @Override
  protected Object getEmailFromOAuth2User() {
    return ((LinkedHashMap) oAuth2User.getAttributes().get("kakao_account")).get("email");
  }

  @Override
  protected SignupPath getSignupPate() {
    return KAKAO;
  }
}
