package everylog.domain.comment.exception;

import everylog.global.error.exception.BusinessException;
import everylog.global.error.exception.ErrorResult;

public class InvalidCommentBlogPostIdInputException extends BusinessException {
    public InvalidCommentBlogPostIdInputException(ErrorResult errorResult) {
        super(errorResult);
    }
}
