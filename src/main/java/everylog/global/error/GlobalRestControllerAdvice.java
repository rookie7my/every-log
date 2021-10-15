package everylog.global.error;

import everylog.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

import static everylog.global.error.exception.ErrorResult.INVALID_INPUT_VALUE;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice(basePackages = {"everylog.domain.comment.controller"})
public class GlobalRestControllerAdvice {

    private final MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException: {}", e.getMessage());
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        List<FieldErrorDto> fieldErrorDtos = getFieldErrorDtos(fieldErrors);
        ErrorResponse errorResponse = ErrorResponse.of(INVALID_INPUT_VALUE, fieldErrorDtos);
        return ResponseEntity.status(INVALID_INPUT_VALUE.getHttpStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        log.error("BusinessException: {}", e.getErrorResult().getMessage());
        ErrorResponse errorResponse = ErrorResponse.from(e.getErrorResult());
        return ResponseEntity.status(e.getErrorResult().getHttpStatus())
                .body(errorResponse);
    }

    private List<FieldErrorDto> getFieldErrorDtos(List<FieldError> fieldErrors) {
        List<FieldErrorDto> fieldErrorDtos = new ArrayList<>();
        for (FieldError fieldError : fieldErrors) {
            String message = messageSource.getMessage(fieldError, null);
            String rejectedValue = "";
            if(fieldError.getRejectedValue() != null){
                rejectedValue = fieldError.getRejectedValue().toString();
            }
            fieldErrorDtos.add(new FieldErrorDto(fieldError.getField(), rejectedValue, message));
        }
        return fieldErrorDtos;
    }
}
