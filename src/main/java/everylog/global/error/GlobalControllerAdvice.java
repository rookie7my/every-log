package everylog.global.error;

import everylog.domain.account.exception.AccountNotFoundException;
import everylog.domain.blogpost.exception.BlogPostNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice(basePackages = {"everylog.domain.account.controller", "everylog.domain.blogpost.controller"})
public class GlobalControllerAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(BlogPostNotFoundException.class)
    public String handleBlogPostNotFoundException(BlogPostNotFoundException e) {
        log.error("BlogPostNotFoundException: {}", e.getErrorResult().getMessage());
        return "error/404";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(AccountNotFoundException.class)
    public String handleAccountNotFoundException(AccountNotFoundException e) {
        log.error("AccountNotFoundException: {}", e.getErrorResult().getMessage());
        return "error/404";
    }
}
