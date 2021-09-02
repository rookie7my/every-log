package everylog.domain.account.controller;

import everylog.domain.account.controller.form.SignUpForm;
import everylog.domain.account.controller.validator.SignUpFormValidator;
import everylog.domain.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class SignUpController {

    private final AccountService accountService;
    private final SignUpFormValidator signUpFormValidator;

    @InitBinder
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
                                BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "account/sign-up-form";
        }
        accountService.createAccount(signUpForm.getUsername(), signUpForm.getEmail(), signUpForm.getPassword());
        return "redirect:/";
    }
}