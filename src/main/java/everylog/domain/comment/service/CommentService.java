package everylog.domain.comment.service;

import everylog.domain.account.domain.Account;
import everylog.domain.account.repository.AccountRepository;
import everylog.domain.blogpost.domain.BlogPost;
import everylog.domain.blogpost.repository.BlogPostRepository;
import everylog.domain.comment.domain.Comment;
import everylog.domain.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final AccountRepository accountRepository;
    private final BlogPostRepository blogPostRepository;

    @Transactional
    public Long createComment(Long writerId, Long blogPostId, String content) {
        Account writer = accountRepository.findById(writerId)
                .orElseThrow(() -> new IllegalArgumentException("writerId is not valid."));

        BlogPost blogPost = blogPostRepository.findById(blogPostId)
                .orElseThrow(() -> new IllegalArgumentException("blogPostId is not valid."));

        Comment comment = new Comment(content, writer, blogPost);
        commentRepository.save(comment);
        return comment.getId();
    }

    @Transactional(readOnly = true)
    public Comment findById(Long id) {
        return commentRepository.findByIdWithWriter(id)
                .orElseThrow(() -> new IllegalArgumentException("id is not valid"));
    }
}
