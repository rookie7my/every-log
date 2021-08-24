package everylog.domain.account.controller.form;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class PasswordUpdateForm {

    @Length(min=8)
    private String password;
}
