package everylog.repository;

import everylog.domain.Account;
import everylog.domain.BlogPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {

    Page<BlogPost> findByAccount(Account account, Pageable pageable);

}
