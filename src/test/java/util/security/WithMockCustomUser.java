package util.security;

import com.withus.withmebe.member.type.Role;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {

  long memberId() default 99L;
  Role role() default Role.ROLE_MEMBER;
  boolean isMobileAuthenticatedMember() default true;

}
