package everylog.domain.comment.service;

import everylog.domain.account.domain.Account;
import everylog.domain.account.exception.AccountNotFoundException;
import everylog.domain.account.repository.AccountRepository;
import everylog.domain.blogpost.domain.BlogPost;
import everylog.domain.blogpost.repository.BlogPostRepository;
import everylog.domain.comment.domain.Comment;
import everylog.domain.comment.exception.InvalidArgumentCommentCreationException;
import everylog.domain.comment.repository.CommentRepository;
import everylog.global.error.exception.ErrorResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @InjectMocks
    CommentService target;

    @Mock
    AccountRepository accountRepository;

    @Mock
    BlogPostRepository blogPostRepository;

    @Mock
    CommentRepository commentRepository;

    Long currentAccountId;
    Long blogPostId;
    String content;

    @BeforeEach
    void init() {
        currentAccountId = 1L;
        blogPostId = 1L;
        content = "test content";
    }

    @DisplayName("댓글 생성 성공")
    @Test
    void commentCreationSuccess() {
        // given
        Account commentWriter = new Account("commentWriter", "commentWriter@test.com", "12345678");
        ReflectionTestUtils.setField(commentWriter, "id", currentAccountId);

        final Long blogPostWriterId = 2L;
        Account blogPostWriter = new Account("blogPostWriter", "blogPostWriter@test.com", "12345678");
        ReflectionTestUtils.setField(blogPostWriter, "id", blogPostWriterId);

        BlogPost blogPost = new BlogPost("testPost", "testContent", "introduction", false, blogPostWriter);
        ReflectionTestUtils.setField(blogPost, "id", blogPostId);

        final Long savedCommentId = -1L;
        Comment savedComment = new Comment(content, commentWriter, blogPost);
        ReflectionTestUtils.setField(savedComment, "id", savedCommentId);

        doReturn(Optional.of(commentWriter))
                .when(accountRepository)
                .findById(currentAccountId);

        doReturn(Optional.of(blogPost))
                .when(blogPostRepository)
                .findById(blogPostId);

        doReturn(savedComment)
                .when(commentRepository)
                .save(any(Comment.class));

        // when
        Comment createdComment = target.createComment(currentAccountId, blogPostId, content);

        // then
        assertThat(createdComment.getId()).isNotNull();
    }

    @DisplayName("댓글 생성 실패: blogPostId에 해당하는 BlogPost 없음")
    @Test
    void blogPostIdInputInvalidThenCommentCreationFail() {
        // given
        final Long invalidBlogPostId = -2L;

        Account writer = new Account("writerName", "writer@writer.com", "1234");
        ReflectionTestUtils.setField(writer, "id", currentAccountId);

        doReturn(Optional.of(writer))
                .when(accountRepository)
                .findById(currentAccountId);

        doReturn(Optional.empty())
                .when(blogPostRepository)
                .findById(invalidBlogPostId);

        // when
        InvalidArgumentCommentCreationException result = catchThrowableOfType(
                () -> target.createComment(currentAccountId, invalidBlogPostId, content),
                InvalidArgumentCommentCreationException.class);

        // then
        assertThat(result.getErrorResult()).isEqualTo(ErrorResult.INVALID_BLOG_POST_ID_FOR_COMMENT_CREATION);
    }

    @DisplayName("댓글 생성 실패: currentAccountId에 해당하는 Account 없음")
    @Test
    void invalidCurrentAccountIdThenCommentCreationFail() {
        // given
        final Long invalidCurrentAccountId = -2L;
        doReturn(Optional.empty())
                .when(accountRepository)
                .findById(invalidCurrentAccountId);

        // when
        AccountNotFoundException result =
                catchThrowableOfType(() -> target.createComment(invalidCurrentAccountId, blogPostId, content),
                        AccountNotFoundException.class);

        // then
        assertThat(result.getErrorResult()).isEqualTo(ErrorResult.INVALID_CURRENT_ACCOUNT_ID);
    }
}