package everylog.global.error;

import everylog.global.error.exception.ErrorCode;
import everylog.global.error.exception.ErrorResult;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class ErrorResponse {
    private final ErrorCode errorCode;
    private final String message;
    private final List<FieldError> errors;

    public static ErrorResponse of(ErrorResult errorResult, BindingResult bindingResult) {
        List<FieldError> errors = bindingResult.getFieldErrors()
                .stream()
                .map(FieldError::from)
                .collect(Collectors.toList());
        return new ErrorResponse(errorResult.getErrorCode(), errorResult.getMessage(), errors);
    }
}
