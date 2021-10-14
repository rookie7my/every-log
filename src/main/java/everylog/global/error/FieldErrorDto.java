package everylog.global.error;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class FieldErrorDto {
    private final String field;
    private final String rejectedValue;
    private final String reason;
}
