package everylog.domain.account.controller;

import everylog.domain.account.controller.form.SignUpForm;
import everylog.domain.account.controller.validator.SignUpFormValidator;
import everylog.domain.account.domain.Account;
import everylog.domain.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final AccountRepository accountRepository;
    private final SignUpFormValidator signUpFormValidator;
    private final PasswordEncoder passwordEncoder;

    @InitBinder("signUpForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signUpFormValidator);
    }

    @GetMapping("/sign-up")
    public String signUpForm(Model model) {
        model.addAttribute(new SignUpForm());
        return "account/sign-up-form";
    }

    @PostMapping("/sign-up")
    public String processSignUp(@ModelAttribute @Valid SignUpForm signUpForm,
                                Errors errors) {
        if(errors.hasErrors()) {
            return "account/sign-up-form";
        }

        Account account = new Account(
                signUpForm.getUsername(),
                signUpForm.getEmail(),
                passwordEncoder.encode(signUpForm.getPassword())
        );

        accountRepository.save(account);
        return "redirect:/";
    }
}