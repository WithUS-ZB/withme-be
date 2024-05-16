package com.withus.withmebe.security.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails, OAuth2User {

  private final transient UserDetailsDomain detailsDomain;

  @Override
  public Map<String, Object> getAttributes() {
    return detailsDomain.attributes().getAttributes();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Collection<GrantedAuthority> list = new ArrayList<>();
    list.add(new SimpleGrantedAuthority(detailsDomain.role().name()));
    return list;
  }

  public Long getMemberId() {
    return detailsDomain.id();
  }

  @Override
  public String getPassword() {
    return detailsDomain.password();
  }

  @Override
  public String getUsername() {
    return detailsDomain.id().toString();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }


  @Override
  public String getName() {
    return detailsDomain.id().toString();
  }

  public boolean getIsMobileAuthenticatedMember(){
    return detailsDomain.isMobileAuthenticatedMember();
  }

  public boolean getIsAdditionalInfoRequired(){
    return detailsDomain.isAdditionalInfoRequired();
  }
}
