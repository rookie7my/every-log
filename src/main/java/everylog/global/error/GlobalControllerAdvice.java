package everylog.global.error;

import everylog.domain.blogpost.exception.BlogPostNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class GlobalControllerAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(BlogPostNotFoundException.class)
    public String BlogPostNotFoundExHandle(BlogPostNotFoundException e) {
        log.error("BlogPostNotFoundException", e);
        return "error/404";
    }
}
