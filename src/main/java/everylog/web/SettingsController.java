package everylog.web;

import everylog.web.form.IntroductionForm;
import everylog.web.form.PasswordUpdateForm;
import everylog.web.form.ShortIntroductionForm;
import everylog.domain.Account;
import everylog.repository.AccountRepository;
import everylog.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/settings")
public class SettingsController {

    private final AccountRepository accountRepository;
    private final AccountService accountService;

    @GetMapping("/introductions")
    public String introductionForm(@CurrentAccountId Long currentAccountId, Model model) {
        Account currentAccount = accountRepository.findById(currentAccountId).orElseThrow();
        model.addAttribute(new ShortIntroductionForm(currentAccount.getShortIntroduction()));
        model.addAttribute(new IntroductionForm(currentAccount.getIntroduction()));
        return "settings/introductions-form";
    }

    @PostMapping("/introductions/short-introduction")
    public String submitShortIntroduction(@CurrentAccountId Long currentAccountId,
                                          @ModelAttribute @Valid ShortIntroductionForm shortIntroductionForm,
                                          BindingResult bindingResult,
                                          Model model) {

        Account currentAccount = accountRepository.findById(currentAccountId).orElseThrow();

        if(bindingResult.hasErrors()) {
            model.addAttribute(new IntroductionForm(currentAccount.getIntroduction()));
            return "settings/introductions-form";
        }

        accountService.updateShortIntroduction(currentAccount, shortIntroductionForm.getShortIntroduction());
        return "redirect:/settings/introductions";
    }

    @PostMapping("/introductions/introduction")
    public String submitIntroduction(@CurrentAccountId Long currentAccountId,
                                     @ModelAttribute IntroductionForm introductionForm) {
        Account currentAccount = accountRepository.findById(currentAccountId).orElseThrow();
        accountService.updateIntroduction(currentAccount, introductionForm.getIntroduction());
        return "redirect:/settings/introductions";
    }

    @GetMapping("/password")
    public String passwordUpdateForm(Model model) {
        model.addAttribute(new PasswordUpdateForm());
        return "settings/password-update-form";
    }

    @PostMapping("/password")
    public String submitPasswordUpdateForm(@CurrentAccountId Long currentAccountId,
                                           @ModelAttribute @Valid PasswordUpdateForm passwordUpdateForm,
                                           BindingResult bindingResult,
                                           RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()) {
            return "settings/password-update-form";
        }
        accountService.updatePassword(currentAccountId, passwordUpdateForm.getPassword());
        redirectAttributes.addFlashAttribute("isPasswordUpdated", true);
        return "redirect:/settings/password";
    }
}
