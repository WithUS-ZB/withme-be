package com.withus.withmebe.security.domain.oauth2;

import com.withus.withmebe.member.entity.Member;
import com.withus.withmebe.member.type.SignupPath;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import org.springframework.security.oauth2.core.user.OAuth2User;

public abstract class OAuth2UserAttributes {

  protected final OAuth2User oAuth2User;
  @Getter
  private final Map<String, Object> attributes = new HashMap<>();
  private final String userNameAttributeName;
  private static final String SOCIAL_PK = "SocialPk";
  private static final String NICKNAME = "nickName";
  private static final String PROFILE_IMAGE = "profileImage";
  private static final String EMAIL = "email";

  protected OAuth2UserAttributes(OAuth2User oAuth2User, String userNameAttributeName) {
    this.oAuth2User = oAuth2User;
    this.userNameAttributeName = userNameAttributeName;
    attributes.put(SOCIAL_PK, getSocialPkFromOAuth2User());
    attributes.put(NICKNAME, getNickNameFromOAuth2User());
    attributes.put(PROFILE_IMAGE, getProfileImageFromOAuth2User());
    attributes.put(EMAIL, getEmailFromOAuth2User());
  }

  // social 사이트의 pk
  private Object getSocialPkFromOAuth2User() {
    return oAuth2User.getAttributes().get(userNameAttributeName);
  }

  protected abstract Object getNickNameFromOAuth2User();

  protected abstract Object getProfileImageFromOAuth2User();

  protected abstract Object getEmailFromOAuth2User();

  protected abstract SignupPath getSignupPate();

  public String getSocialPk() {
    return attributes.get(SOCIAL_PK).toString();
  }

  public String getNickName() {
    return attributes.get(NICKNAME).toString();
  }

  public String getProfileImage() {
    return attributes.get(PROFILE_IMAGE).toString();
  }

  public String getEmail() {
    return attributes.get(EMAIL).toString();
  }

  public Member toEntity() {
    Member member = Member.builder()
        .email(getEmail())
        .nickName(getEmail())
        .signupDttm(LocalDateTime.now())
        .build();
    member.setSignupPath(getSignupPate());
    member.setProfileImg(getProfileImage());
    return member;
  }
}
