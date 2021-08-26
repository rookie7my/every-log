package everylog.domain.blogpost.repository;

import everylog.domain.account.domain.Account;
import everylog.domain.blogpost.domain.BlogPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {

    Page<BlogPost> findByWriter(Account writer, Pageable pageable);

    @Query("select bp from BlogPost bp join fetch bp.writer w where bp.id = :id")
    BlogPost findByIdWithWriter(@Param("id")Long blogPostId);
}
