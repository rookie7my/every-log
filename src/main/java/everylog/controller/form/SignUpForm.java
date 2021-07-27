package everylog.controller.form;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class SignUpForm {

    @NotBlank
    @Length(min=2, max=30)
    @Pattern(regexp = "^[a-zA-Z0-9가-힣-_]{2,30}$")
    private String username;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Length(min=8)
    private String password;

}
