package everylog.domain.account.service;

import everylog.domain.account.domain.Account;
import everylog.domain.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(usernameOrEmail);
        if(account == null) {
            account = accountRepository.findByEmail(usernameOrEmail);
        }

        if(account == null) {
            throw new UsernameNotFoundException(usernameOrEmail);
        }
        return new AccountUserDetails(account);
    }

    @Transactional
    public void updateShortIntroduction(Account account, String shortIntroduction) {
        account.updateShortIntroduction(shortIntroduction);
    }

    @Transactional
    public void updateIntroduction(Account account, String introduction) {
        account.updateIntroduction(introduction);
    }


    @Transactional
    public void updatePassword(Long currentAccountId, String rawPassword) {
        Account account = accountRepository.findById(currentAccountId).orElseThrow();
        account.updatePassword(passwordEncoder.encode(rawPassword));
    }

    @Transactional
    public Long createAccount(String username, String email, String rawPassword) {
        String encodedPassword = passwordEncoder.encode(rawPassword);
        Account account = new Account(username, email, encodedPassword);
        return accountRepository.save(account).getId();
    }
}