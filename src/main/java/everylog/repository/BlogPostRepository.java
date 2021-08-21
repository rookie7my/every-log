package everylog.repository;

import everylog.domain.Account;
import everylog.domain.BlogPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {

    Page<BlogPost> findByAccount(Account account, Pageable pageable);

    @Query("select bp from BlogPost bp join fetch bp.account a where bp.id = :id")
    BlogPost findByIdWithAccount(@Param("id")Long blogPostId);
}
