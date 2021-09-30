package everylog.global.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static everylog.global.error.exception.ErrorResult.INVALID_INPUT_VALUE;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class GlobalRestControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException: {}", e.getMessage());
        ErrorResponse errorResponse = ErrorResponse.of(INVALID_INPUT_VALUE, e.getBindingResult());
        return ResponseEntity.status(INVALID_INPUT_VALUE.getHttpStatus())
                .body(errorResponse);
    }
}
