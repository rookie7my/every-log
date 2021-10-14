package everylog.domain.comment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import everylog.domain.account.domain.Account;
import everylog.domain.account.exception.AccountNotFoundException;
import everylog.domain.blogpost.domain.BlogPost;
import everylog.domain.comment.domain.Comment;
import everylog.domain.comment.repository.CommentRepository;
import everylog.domain.comment.service.CommentService;
import everylog.global.error.ErrorResponse;
import everylog.global.error.FieldErrorDto;
import everylog.global.error.exception.ErrorCode;
import everylog.global.error.exception.ErrorResult;
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

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
class CommentControllerTest {

    public static final long CURRENT_ACCOUNT_ID = 2L;
    public static final String CURRENT_ACCOUNT_USERNAME = "test-user";
    public static final String CURRENT_ACCOUNT_PASSWORD = "12345678";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CommentService commentService;

    @MockBean
    CommentRepository commentRepository;

    ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @DisplayName("댓글 조회 실패: blogPostId 파라미터 없음")
    @Test
    void searchForCommentsWithoutBlogPostIdThenFail() throws Exception {
        // given
        final String url = "/api/comments";

        // when
        ResultActions resultActions = mockMvc.perform(get(url));

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @DisplayName("댓글 조회 성공")
    @Test
    void searchForCommentsWithBlogPostIdThenReturnResults() throws Exception {
        // given
        final String url = "/api/comments";
        final Long blogPostId = 1L;

        final Account writer1 = new Account("writer1", "writer1@writer1.com", "12345678");
        final Account writer2 = new Account("writer2", "writer2@writer2.com", "12345678");
        final Account writer = new Account("writer", "writer@writer.com", "12345678");

        final BlogPost blogPost =
                new BlogPost("title", "content", "introduction", false, writer);
        ReflectionTestUtils.setField(blogPost, "id", blogPostId);

        final Comment comment1 = new Comment("commentContent1", writer1, blogPost);
        final Comment comment2 = new Comment("commentContent2", writer2, blogPost);
        final List<Comment> results = List.of(comment1, comment2);

        given(commentRepository.findAllByBlogPostId(blogPostId))
                .willReturn(results);

        // when
        ResultActions resultActions = mockMvc.perform(get(url)
                .param("blogPostId", String.valueOf(blogPostId)));

        // then
        resultActions.andExpect(status().isOk());
        String responseBody = resultActions.andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        CommentQueryResponseDto commentQueryResponseDto =
                objectMapper.readValue(responseBody, CommentQueryResponseDto.class);

        assertThat(commentQueryResponseDto.getCount()).isEqualTo(results.size());
    }

    @DisplayName("댓글 생성 실패: commentService 가 BusinessException 을 던지는 경우")
    @Test
    @WithMockAccountUserDetails(id = CURRENT_ACCOUNT_ID)
    void commentServiceThrowsBusinessExceptionThenCommentCreationFail() throws Exception {
        // given
        final String url = "/api/comments";
        final Long blogPostId = 1L;
        final String commentContent = "test-content";
        CommentCreationRequestDto commentCreationRequestDto = new CommentCreationRequestDto(blogPostId, commentContent);

        given(commentService.createComment(CURRENT_ACCOUNT_ID, blogPostId, commentContent))
                .willThrow(new AccountNotFoundException(ErrorResult.INVALID_CURRENT_ACCOUNT_ID));

        // when
        ResultActions resultActions = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentCreationRequestDto))
                .with(csrf()));

        // then
        resultActions.andExpect(status().is(ErrorResult.INVALID_CURRENT_ACCOUNT_ID.getHttpStatus().value()));

        String responseBody = resultActions.andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        ErrorResponse errorResponse = objectMapper.readValue(responseBody, ErrorResponse.class);
        assertThat(errorResponse.getErrorCode()).isEqualTo(ErrorResult.INVALID_CURRENT_ACCOUNT_ID.getErrorCode());
        assertThat(errorResponse.getMessage()).isEqualTo(ErrorResult.INVALID_CURRENT_ACCOUNT_ID.getMessage());
        assertThat(errorResponse.getErrors()).isEmpty();
    }

    @DisplayName("댓글 생성 실패: blogPostId가 Null")
    @Test
    @WithMockAccountUserDetails
    void blogPostIdIsNullThenCommentCreationFail() throws Exception {
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

        String responseBody = resultActions.andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        ErrorResponse errorResponse = objectMapper.readValue(responseBody, ErrorResponse.class);
        assertThat(errorResponse.getErrorCode()).isEqualTo(ErrorCode.COMMON_1);
        assertThat(errorResponse.getMessage()).isEqualTo(ErrorResult.INVALID_INPUT_VALUE.getMessage());
        assertThat(errorResponse.getErrors().size()).isEqualTo(1);

        FieldErrorDto fieldErrorDto = errorResponse.getErrors().get(0);
        assertThat(fieldErrorDto.getField()).isEqualTo("blogPostId");
        assertThat(fieldErrorDto.getRejectedValue()).isEqualTo("");
    }

    @DisplayName("댓글 생성 실패: 빈 댓글 내용")
    @Test
    @WithMockAccountUserDetails
    void emptyContentThenCommentCreationFail() throws Exception {
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

        String responseBody = resultActions.andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        ErrorResponse errorResponse = objectMapper.readValue(responseBody, ErrorResponse.class);

        assertThat(errorResponse.getErrorCode()).isEqualTo(ErrorCode.COMMON_1);
        assertThat(errorResponse.getMessage()).isEqualTo(ErrorResult.INVALID_INPUT_VALUE.getMessage());
        assertThat(errorResponse.getErrors().size()).isEqualTo(1);

        FieldErrorDto fieldErrorDto = errorResponse.getErrors().get(0);
        assertThat(fieldErrorDto.getField()).isEqualTo("content");
        assertThat(fieldErrorDto.getRejectedValue()).isEqualTo("");
    }

    @DisplayName("댓글 생성 실패: 로그인 안한 유저가 요청")
    @Test
    void userNotLoggedInThenCommentCreationFail() throws Exception {
        // given
        final String url = "/api/comments";
        final Long blogPostId = 1L;
        final String commentContent = "test-content";
        CommentCreationRequestDto commentCreationRequestDto = new CommentCreationRequestDto(blogPostId, commentContent);

        // when
        ResultActions resultActions = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentCreationRequestDto))
                .with(csrf())
        );

        // then
        resultActions.andExpect(status().isUnauthorized())
                .andExpect(header().string("WWW-Authenticate", "formBased"));
    }

    @DisplayName("댓글 생성 성공")
    @Test
    @WithMockAccountUserDetails(id = CURRENT_ACCOUNT_ID,
            username = CURRENT_ACCOUNT_USERNAME,
            password = CURRENT_ACCOUNT_PASSWORD)
    void commentCreationSuccess() throws Exception {
        // given
        final String url = "/api/comments";
        final Long blogPostId = 1L;
        final String commentContent = "test-content";
        final Long savedCommentId = 1L;

        CommentCreationRequestDto commentCreationRequestDto =
                new CommentCreationRequestDto(blogPostId, commentContent);

        Account currentAccount = new Account(CURRENT_ACCOUNT_USERNAME, "test@test.com", CURRENT_ACCOUNT_PASSWORD);
        Account blogWriter = new Account("blog-writer", "blog@blog.com", "12345678");
        BlogPost blogPost = new BlogPost("test", "test", "test", false, blogWriter);
        Comment savedComment = new Comment(commentContent, currentAccount, blogPost);
        ReflectionTestUtils.setField(savedComment, "id", savedCommentId);

        given(commentService.createComment(CURRENT_ACCOUNT_ID, blogPostId, commentContent))
                .willReturn(savedComment);

        // when
        ResultActions resultActions = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentCreationRequestDto))
                .with(csrf()));

        // then
        resultActions.andExpect(status().isCreated());

        String responseBody = resultActions.andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        CommentCreationResponseDto result = objectMapper.readValue(responseBody, CommentCreationResponseDto.class);
        assertThat(result.getContent()).isEqualTo(commentContent);
        assertThat(result.getWriterName()).isEqualTo(CURRENT_ACCOUNT_USERNAME);
    }
}