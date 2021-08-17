package everylog.repository;

import everylog.domain.Account;
import everylog.domain.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {

    List<BlogPost> findByAccount(Account account);

}
