package com.withus.withmebe.security.service;

import com.withus.withmebe.common.exception.ExceptionCode;
import com.withus.withmebe.member.repository.MemberRepository;
import com.withus.withmebe.security.domain.CustomUserDetails;
import com.withus.withmebe.security.domain.UserDetailsDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final MemberRepository memberRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return new CustomUserDetails(getUserDetailsDomain(username));
  }

  private UserDetailsDomain getUserDetailsDomain(String username) {
    return UserDetailsDomain.fromEntity(memberRepository.findById(Long.valueOf(username))
        .orElseThrow(() -> new UsernameNotFoundException(
            ExceptionCode.ENTITY_NOT_FOUND.getMessage()
        )), null);
  }
}
