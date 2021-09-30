package everylog.global.error;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class FieldError {
    private final String field;
    private final String rejectedValue;
    private final String reason;

    public static FieldError from(org.springframework.validation.FieldError fieldError) {
        String value = "";
        if(fieldError.getRejectedValue() != null){
            value = fieldError.getRejectedValue().toString();
        }
        return new FieldError(fieldError.getField(), value, fieldError.getDefaultMessage());
    }
}
