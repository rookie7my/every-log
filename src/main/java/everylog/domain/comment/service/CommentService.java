package everylog.domain.comment.service;

import everylog.domain.account.domain.Account;
import everylog.domain.account.repository.AccountRepository;
import everylog.domain.blogpost.domain.BlogPost;
import everylog.domain.blogpost.repository.BlogPostRepository;
import everylog.domain.comment.domain.Comment;
import everylog.domain.comment.exception.InvalidArgumentCommentCreationException;
import everylog.domain.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static everylog.global.error.exception.ErrorResult.INVALID_BLOG_POST_ID_FOR_COMMENT_CREATION;
import static everylog.global.error.exception.ErrorResult.INVALID_WRITER_ID_FOR_COMMENT_CREATION;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final AccountRepository accountRepository;
    private final BlogPostRepository blogPostRepository;

    @Transactional
    public Long createComment(Long writerId, Long blogPostId, String content) {
        Account writer = accountRepository.findById(writerId)
                .orElseThrow(() -> new InvalidArgumentCommentCreationException(INVALID_WRITER_ID_FOR_COMMENT_CREATION));

        BlogPost blogPost = blogPostRepository.findById(blogPostId)
                .orElseThrow(() -> new InvalidArgumentCommentCreationException(INVALID_BLOG_POST_ID_FOR_COMMENT_CREATION));

        Comment comment = new Comment(content, writer, blogPost);
        return commentRepository.save(comment).getId();
    }

    @Transactional(readOnly = true)
    public Comment findById(Long id) {
        return commentRepository.findByIdWithWriter(id)
                .orElseThrow(() -> new IllegalArgumentException("id is not valid"));
    }

    @Transactional(readOnly = true)
    public List<Comment> findAllByBlogPost(Long blogPostId) {
        return commentRepository.findAllByBlogPost(blogPostId);
    }
}
