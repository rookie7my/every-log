package everylog.controller;

import everylog.controller.form.IntroductionForm;
import everylog.controller.form.ShortIntroductionForm;
import everylog.domain.Account;
import everylog.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/settings")
public class SettingsController {

    private final AccountRepository accountRepository;

    @GetMapping("/introductions")
    public String introductionForm(@AuthenticationPrincipal User user, Model model) {
        String username = user.getUsername();
        Account currentAccount = accountRepository.findByUsername(username);
        model.addAttribute(new ShortIntroductionForm(currentAccount.getShortIntroduction()));
        model.addAttribute(new IntroductionForm(currentAccount.getIntroduction()));
        return "settings/introductions-form";
    }
}
