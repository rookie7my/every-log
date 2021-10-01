package everylog.global.security;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockAccountUserDetailsSecurityContextFactory.class)
public @interface WithMockAccountUserDetails {
    long id() default 1L;
    String username() default "test-user";
    String password() default "12345678";
}
