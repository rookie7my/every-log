package everylog.domain.comment.repository;

import everylog.domain.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c join fetch c.writer where c.id = :id")
    Optional<Comment> findByIdWithWriter(@Param("id") Long id);

    @Query("select c from Comment c" +
           " join fetch c.writer" +
           " join c.blogPost bp" +
           " where bp.id = :blogPostId")
    List<Comment> findAllByBlogPost(@Param("blogPostId") Long blogPostId);
}