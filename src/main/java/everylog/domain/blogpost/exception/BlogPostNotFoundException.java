package everylog.domain.blogpost.exception;

import everylog.global.error.exception.BusinessException;
import everylog.global.error.exception.ErrorResult;

public class BlogPostNotFoundException extends BusinessException {
    public BlogPostNotFoundException(ErrorResult errorResult) {
        super(errorResult);
    }
}
