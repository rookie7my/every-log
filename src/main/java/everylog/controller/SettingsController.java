package everylog.controller;

import everylog.controller.form.IntroductionForm;
import everylog.controller.form.ShortIntroductionForm;
import everylog.domain.Account;
import everylog.repository.AccountRepository;
import everylog.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/settings")
public class SettingsController {

    private final AccountRepository accountRepository;
    private final AccountService accountService;

    @GetMapping("/introductions")
    public String introductionForm(@AuthenticationPrincipal User user, Model model) {
        String username = user.getUsername();
        Account currentAccount = accountRepository.findByUsername(username);
        model.addAttribute(new ShortIntroductionForm(currentAccount.getShortIntroduction()));
        model.addAttribute(new IntroductionForm(currentAccount.getIntroduction()));
        return "settings/introductions-form";
    }

    @PostMapping("/introductions/short-introduction")
    public String submitShortIntroduction(@AuthenticationPrincipal User user,
                                          @ModelAttribute @Valid ShortIntroductionForm shortIntroductionForm,
                                          BindingResult bindingResult,
                                          Model model) {

        String username = user.getUsername();
        Account currentAccount = accountRepository.findByUsername(username);

        if(bindingResult.hasErrors()) {
            model.addAttribute(new IntroductionForm(currentAccount.getIntroduction()));
            return "settings/introductions-form";
        }

        accountService.updateShortIntroduction(currentAccount, shortIntroductionForm.getShortIntroduction());
        return "redirect:/settings/introductions";
    }

    @PostMapping("/introductions/introduction")
    public String submitIntroduction(@AuthenticationPrincipal User user,
                                     @ModelAttribute IntroductionForm introductionForm) {
        String username = user.getUsername();
        Account currentAccount = accountRepository.findByUsername(username);
        accountService.updateIntroduction(currentAccount, introductionForm.getIntroduction());
        return "redirect:/settings/introductions";
    }
}
