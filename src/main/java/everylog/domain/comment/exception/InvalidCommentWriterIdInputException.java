package everylog.domain.comment.exception;

import everylog.global.error.exception.BusinessException;
import everylog.global.error.exception.ErrorResult;

public class InvalidCommentWriterIdInputException extends BusinessException {
    public InvalidCommentWriterIdInputException(ErrorResult errorResult) {
        super(errorResult);
    }
}
