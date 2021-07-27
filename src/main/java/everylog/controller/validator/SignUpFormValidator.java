package everylog.controller.validator;

import everylog.controller.form.SignUpForm;
import everylog.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class SignUpFormValidator implements Validator {

    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return SignUpForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SignUpForm signUpForm = (SignUpForm) target;
        String username = signUpForm.getUsername();
        String email = signUpForm.getEmail();

        if(accountRepository.existsByUsername(username)) {
            errors.rejectValue("username", "unique");
        }

        if(accountRepository.existsByEmail(email)) {
            errors.rejectValue("email", "unique");
        }
    }
}
