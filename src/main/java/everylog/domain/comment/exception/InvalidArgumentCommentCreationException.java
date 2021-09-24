package everylog.domain.comment.exception;

import everylog.global.error.exception.BusinessException;
import everylog.global.error.exception.ErrorResult;

public class InvalidArgumentCommentCreationException extends BusinessException {
    public InvalidArgumentCommentCreationException(ErrorResult errorResult) {
        super(errorResult);
    }
}
