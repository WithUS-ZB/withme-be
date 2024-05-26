package util.security;

import com.withus.withmebe.member.type.Role;
import com.withus.withmebe.security.domain.CustomUserDetails;
import com.withus.withmebe.security.domain.UserDetailsDomain;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.stereotype.Service;

@Service
final class WithMockCustomUserSecurityContextFactory implements
    WithSecurityContextFactory<WithMockCustomUser> {

  @Override
  public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
    CustomUserDetails customUserDetails = new CustomUserDetails(
        new UserDetailsDomain(customUser.memberId(), "", Role.ROLE_MEMBER, null, customUser.isMobileAuthenticatedMember(), true));
    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
        customUserDetails, "",
        customUserDetails.getAuthorities());
    securityContext.setAuthentication(authenticationToken);
    return securityContext;
  }
}
