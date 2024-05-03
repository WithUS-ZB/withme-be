package com.withus.withmebe.security.domain;

import java.util.ArrayList;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

  private final transient UserDetailsDomain details;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Collection<GrantedAuthority> list = new ArrayList<>();
    list.add(new SimpleGrantedAuthority(details.role().name()));
    return list;
  }

  public Long getMemberId() {
    return details.id();
  }

  @Override
  public String getPassword() {
    return details.password();
  }

  @Override
  public String getUsername() {
    return details.id().toString();
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

}
