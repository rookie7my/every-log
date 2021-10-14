package everylog.global.error;

import everylog.global.error.exception.ErrorCode;
import everylog.global.error.exception.ErrorResult;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static java.util.Collections.emptyList;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class ErrorResponse {
    private final ErrorCode errorCode;
    private final String message;
    private final List<FieldErrorDto> errors;

    public static ErrorResponse of(ErrorResult errorResult, List<FieldErrorDto> fieldErrorDtos) {
        return new ErrorResponse(errorResult.getErrorCode(), errorResult.getMessage(), fieldErrorDtos);
    }

    public static ErrorResponse from(ErrorResult errorResult) {
        return new ErrorResponse(errorResult.getErrorCode(), errorResult.getMessage(), emptyList());
    }
}
