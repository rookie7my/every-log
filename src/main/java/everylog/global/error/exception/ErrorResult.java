package everylog.global.error.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorResult {

    INVALID_WRITER_ID_FOR_COMMENT_CREATION(HttpStatus.BAD_REQUEST,
            "when creating comment, writer with given writerId does not exist"),
    INVALID_BLOG_POST_ID_FOR_COMMENT_CREATION(HttpStatus.BAD_REQUEST,
            "when creating comment, blogPost with given blogPostId does not exist"),


    ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "Requested account can not be found"),


    BLOG_POST_NOT_FOUND(HttpStatus.NOT_FOUND, "Requested blogPost can not be found");

    private final HttpStatus httpStatus;
    private final String message;
}
