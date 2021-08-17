package everylog.repository;

import everylog.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Account findByUsername(String username);

    Account findByEmail(String email);
}
