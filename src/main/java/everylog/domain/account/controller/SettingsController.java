package everylog.domain.account.controller;

import everylog.domain.account.controller.form.IntroductionForm;
import everylog.domain.account.controller.form.PasswordUpdateForm;
import everylog.domain.account.controller.form.ShortIntroductionForm;
import everylog.domain.account.domain.Account;
import everylog.domain.account.repository.AccountRepository;
import everylog.domain.account.service.AccountService;
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

    @GetMapping
    public String getSettingsHome(@CurrentAccountId Long currentAccountId, Model model) {
        Account currentAccount = accountRepository.findById(currentAccountId).orElseThrow();
        model.addAttribute("currentAccount", currentAccount);
        return "settings/home";
    }

    @GetMapping("/short-introduction")
    public String shortIntroductionForm(@CurrentAccountId Long currentAccountId, Model model) {
        Account currentAccount = accountRepository.findById(currentAccountId).orElseThrow();
        model.addAttribute(new ShortIntroductionForm(currentAccount.getShortIntroduction()));
        return "settings/short-introduction-form";
    }

    @PostMapping("/short-introduction")
    public String submitShortIntroduction(@CurrentAccountId Long currentAccountId,
                                          @ModelAttribute @Valid ShortIntroductionForm shortIntroductionForm,
                                          BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            return "settings/short-introduction-form";
        }

        Account currentAccount = accountRepository.findById(currentAccountId).orElseThrow();
        accountService.updateShortIntroduction(currentAccount, shortIntroductionForm.getShortIntroduction());
        return "redirect:/settings";
    }

    @GetMapping("/introduction")
    public String introductionForm(@CurrentAccountId Long currentAccountId, Model model) {
        Account currentAccount = accountRepository.findById(currentAccountId).orElseThrow();
        model.addAttribute(new IntroductionForm(currentAccount.getIntroduction()));
        return "settings/introduction-form";
    }

    @PostMapping("/introduction")
    public String submitIntroduction(@CurrentAccountId Long currentAccountId,
                                     @ModelAttribute IntroductionForm introductionForm) {
        Account currentAccount = accountRepository.findById(currentAccountId).orElseThrow();
        accountService.updateIntroduction(currentAccount, introductionForm.getIntroduction());
        return "redirect:/settings/introduction";
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
