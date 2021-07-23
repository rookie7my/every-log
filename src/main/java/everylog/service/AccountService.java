package everylog.service;

import everylog.domain.Account;
import everylog.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(usernameOrEmail);
        if(account == null) {
            account = accountRepository.findByEmail(usernameOrEmail);
        }

        if(account == null) {
            throw new UsernameNotFoundException(usernameOrEmail);
        }

        return User.builder()
                .username(account.getUsername())
                .password(account.getPassword())
                .roles("USER")
                .build();
    }

    @Transactional
    public void updateShortIntroduction(Account account, String shortIntroduction) {
        account.updateShortIntroduction(shortIntroduction);
    }
}
