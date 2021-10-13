package everylog.domain.comment.repository;

import everylog.domain.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c" +
            " join fetch c.writer w" +
            " join c.blogPost bp" +
            " where bp.id = :blogPostId" +
            " order by c.id")
    List<Comment> findAllByBlogPostId(@Param("blogPostId") Long blogPostId);
}
