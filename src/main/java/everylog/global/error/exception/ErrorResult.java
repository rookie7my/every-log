package everylog.global.error.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorResult {

    INVALID_COMMENT_WRITER_ID_INPUT(HttpStatus.BAD_REQUEST, "writerId to create comment is invalid"),
    INVALID_COMMENT_BLOG_POST_ID_INPUT(HttpStatus.BAD_REQUEST, "blogPostId to create comment is invalid"),

    ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "Requested account can not be found"),

    BLOG_POST_NOT_FOUND(HttpStatus.NOT_FOUND, "Requested blogPost can not be found");

    private final HttpStatus httpStatus;
    private final String message;
}
