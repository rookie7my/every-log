package everylog.global.error.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum ErrorResult {

    INVALID_WRITER_ID_FOR_COMMENT_CREATION(BAD_REQUEST,
            "when creating comment, writer with given writerId does not exist"),
    INVALID_BLOG_POST_ID_FOR_COMMENT_CREATION(BAD_REQUEST,
            "when creating comment, blogPost with given blogPostId does not exist"),


    ACCOUNT_NOT_FOUND(NOT_FOUND, "Requested account can not be found"),
    INVALID_CURRENT_ACCOUNT_ID(INTERNAL_SERVER_ERROR, "currentAccountId is invalid"),

    BLOG_POST_NOT_FOUND(NOT_FOUND, "Requested blogPost can not be found");

    private final HttpStatus httpStatus;
    private final String message;
}
