package everylog.domain.comment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import everylog.domain.account.domain.Account;
import everylog.domain.blogpost.domain.BlogPost;
import everylog.domain.comment.domain.Comment;
import everylog.domain.comment.service.CommentService;
import everylog.global.security.WithMockAccountUserDetails;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
class CommentControllerTest {

    public static final long LOGIN_ACCOUNT_ID = 2L;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CommentService commentService;

    ObjectMapper objectMapper = new ObjectMapper();

    @DisplayName("댓글 생성 실패: blogPostId가 Null")
    @Test
    @WithMockAccountUserDetails
    void BlogPostIdIsNullThenCommentCreationFail() throws Exception {
        // given
        final String url = "/api/comments";
        CommentCreationRequestDto commentCreationRequestDto =
                new CommentCreationRequestDto(null, "test-content");

        // when
        ResultActions resultActions = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentCreationRequestDto))
                .with(csrf()));

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @DisplayName("댓글 생성 실패: 빈 댓글 내용")
    @Test
    @WithMockAccountUserDetails
    void EmptyContentThenCommentCreationFail() throws Exception {
        // given
        final String url = "/api/comments";
        CommentCreationRequestDto commentCreationRequestDto =
                new CommentCreationRequestDto(1L, "");

        // when
        ResultActions resultActions = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentCreationRequestDto))
                .with(csrf()));

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @DisplayName("로그인 페이지로 리다이렉트: 로그인 안한 사용자가 댓글 생성 요청")
    @Test
    void userNotLoggedInThenRedirectLoginPage() throws Exception {
        // given
        final String url = "/api/comments";
        CommentCreationRequestDto commentCreationRequestDto =
                new CommentCreationRequestDto(1L, "test-content");

        // when
        ResultActions resultActions = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentCreationRequestDto))
                .with(csrf()));

        // then
        resultActions.andExpect(status().isSeeOther());
    }

    @DisplayName("댓글 생성 성공")
    @Test
    @WithMockAccountUserDetails(id = LOGIN_ACCOUNT_ID)
    void CommentCreationSuccess() throws Exception {
        // given
        final String url = "/api/comments";
        final Long blogPostId = 1L;
        final String content = "test-content";
        final Long savedCommentId = 1L;

        CommentCreationRequestDto commentCreationRequestDto =
                new CommentCreationRequestDto(blogPostId, content);

        given(commentService.createComment(LOGIN_ACCOUNT_ID, blogPostId, content))
                .willReturn(savedCommentId);

        Account commentWriter = new Account("test-user", "test@test.com", "12345678");
        Account blogWriter = new Account("blog-writer", "blog@blog.com", "12345678");
        BlogPost blogPost = new BlogPost("test", "test", "test", false, blogWriter);
        Comment savedComment = new Comment("test-content", commentWriter, blogPost);
        ReflectionTestUtils.setField(savedComment, "id", savedCommentId);

        given(commentService.findById(savedCommentId))
                .willReturn(savedComment);

        // when
        ResultActions resultActions = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentCreationRequestDto))
                .with(csrf()));

        // then
        resultActions.andExpect(status().isCreated());
    }
}