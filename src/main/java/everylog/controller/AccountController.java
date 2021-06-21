package everylog.controller;

import everylog.domain.Account;
import everylog.repository.AccountRepository;
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

    @GetMapping("/@{name}")
    public String accountHome(@PathVariable String name, Model model) {
        model.addAttribute("name", name);
        return "account/home";
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

        Account account = Account.builder()
                .username(signUpForm.getUsername())
                .email(signUpForm.getEmail())
                .password(passwordEncoder.encode(signUpForm.getPassword()))
                .build();

        accountRepository.save(account);
        return "redirect:/";
    }
}
