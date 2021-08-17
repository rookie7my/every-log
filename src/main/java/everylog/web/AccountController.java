package everylog.web;

import everylog.domain.BlogPost;
import everylog.repository.BlogPostRepository;
import everylog.web.form.SignUpForm;
import everylog.web.validator.SignUpFormValidator;
import everylog.domain.Account;
import everylog.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final AccountRepository accountRepository;
    private final SignUpFormValidator signUpFormValidator;
    private final PasswordEncoder passwordEncoder;
    private final BlogPostRepository blogPostRepository;

    @InitBinder("signUpForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signUpFormValidator);
    }

    @GetMapping("/@{username}")
    public String accountHome(@PathVariable String username, Model model) {
        Account homeOwner = accountRepository.findByUsername(username);
        if(homeOwner == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "exist",
                    new IllegalArgumentException("An account with given username does not exist.")
            );
        }
        List<BlogPost> blogPosts = blogPostRepository.findByAccount(homeOwner);
        model.addAttribute("homeOwner", homeOwner);
        model.addAttribute("blogPosts", blogPosts);
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

        Account account = new Account(
                signUpForm.getUsername(),
                signUpForm.getEmail(),
                passwordEncoder.encode(signUpForm.getPassword())
        );

        accountRepository.save(account);
        return "redirect:/";
    }
}
