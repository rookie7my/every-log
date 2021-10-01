package everylog.global.security;

import everylog.domain.account.domain.Account;
import everylog.domain.account.service.AccountUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.test.util.ReflectionTestUtils;

public class WithMockAccountUserDetailsSecurityContextFactory
        implements WithSecurityContextFactory<WithMockAccountUserDetails> {

    @Override
    public SecurityContext createSecurityContext(WithMockAccountUserDetails withMockAccountUserDetails) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        Account account =
                new Account(withMockAccountUserDetails.username(),
                "test@test.com",
                withMockAccountUserDetails.password());
        ReflectionTestUtils.setField(account, "id", withMockAccountUserDetails.id());
        AccountUserDetails principal = new AccountUserDetails(account);

        UsernamePasswordAuthenticationToken authentication
                = new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities());
        context.setAuthentication(authentication);
        return context;
    }
}
