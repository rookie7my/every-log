package everylog.controller;

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
            errors.rejectValue("username", "wrong.username", "이미 사용중인 username 입니다.");
        }

        if(accountRepository.existsByEmail(email)) {
            errors.rejectValue("email", "wrong.email", "이미 사용중인 email 입니다.");
        }
    }
}
