package everylog.domain.account.service;

import everylog.domain.account.domain.Account;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;


public class AccountUserDetails extends User {

    private Long id;

    public AccountUserDetails(Account account) {
        super(account.getUsername(), account.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_USER")));
        this.id = account.getId();
    }

    public Long getId() {
        return id;
    }
}
