package everylog.domain.blogpost.exception;

public class BlogPostNotFoundException extends RuntimeException {
    public BlogPostNotFoundException() {
        super();
    }

    public BlogPostNotFoundException(String message) {
        super(message);
    }

    public BlogPostNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public BlogPostNotFoundException(Throwable cause) {
        super(cause);
    }

    protected BlogPostNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
