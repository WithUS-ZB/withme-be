package com.withus.withmebe.security.service;

import com.withus.withmebe.member.entity.Member;
import com.withus.withmebe.member.repository.MemberRepository;
import com.withus.withmebe.security.domain.CustomUserDetails;
import com.withus.withmebe.security.domain.UserDetailsDomain;
import com.withus.withmebe.security.domain.oauth2.OAuth2UserAttributes;
import com.withus.withmebe.security.domain.oauth2.OAuth2UserAttributesCreater;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {
  private final MemberRepository memberRepository;
  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2UserAttributes attributes = OAuth2UserAttributesCreater.create(
        getRegistrationId(userRequest),
        super.loadUser(userRequest),
        getUserNameAttributeName(userRequest)
    );
    Member member = memberRepository.findByEmail(attributes.getEmail()).orElseGet(
        () -> memberRepository.save(attributes.toEntity())
    );

    return new CustomUserDetails(
        UserDetailsDomain.fromEntity(member, attributes));
  }

  private static String getRegistrationId(OAuth2UserRequest userRequest) {
    return userRequest.getClientRegistration().getRegistrationId();
  }

  private static String getUserNameAttributeName(OAuth2UserRequest userRequest) {
    return userRequest.getClientRegistration()
        .getProviderDetails()
        .getUserInfoEndpoint()
        .getUserNameAttributeName();
  }
}