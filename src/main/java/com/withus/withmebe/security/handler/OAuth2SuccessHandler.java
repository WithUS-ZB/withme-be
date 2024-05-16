package com.withus.withmebe.security.handler;

import com.withus.withmebe.security.domain.CustomUserDetails;
import com.withus.withmebe.security.jwt.provider.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
  private final TokenProvider tokenProvider;
  @Value("${front.url}")
  private String frontUrl;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException {
    CustomUserDetails oAuth2User = (CustomUserDetails) authentication.getPrincipal();

    String targetUrl = UriComponentsBuilder.fromUriString(frontUrl + "/auth/success?")
        .queryParam("Authorization", "Bearer "+ getToken(oAuth2User))
        .queryParam("isAdditionalInfoRequired", oAuth2User.getIsAdditionalInfoRequired())
        .build().toUriString();

    response.sendRedirect(targetUrl);
  }

  private String getToken(CustomUserDetails oAuth2User) {
    return tokenProvider.generateToken(
        oAuth2User.getUsername()
        , oAuth2User.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority).toList());
  }
}
