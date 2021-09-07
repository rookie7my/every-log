package everylog.domain.blogpost.controller;

import everylog.domain.account.domain.Account;
import everylog.domain.account.repository.AccountRepository;
import everylog.domain.blogpost.service.BlogPostCreationDto;
import everylog.domain.blogpost.service.BlogPostService;
import everylog.domain.account.controller.CurrentAccountId;
import everylog.domain.blogpost.controller.form.BlogPostForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class BlogPostCreationController {

    private final AccountRepository accountRepository;
    private final BlogPostService blogPostService;

    @GetMapping("/new")
    public String createBlogPostForm(Model model) {
        model.addAttribute(new BlogPostForm());
        return "blogpost/form";
    }

    @PostMapping("/new")
    public String createBlogPost(@CurrentAccountId Long currentAccountId,
                                 @ModelAttribute @Valid BlogPostForm blogPostForm,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()) {
            return "blogpost/form";
        }

        BlogPostCreationDto blogPostCreationDto = new BlogPostCreationDto(blogPostForm.getTitle()
                , blogPostForm.getContent()
                , blogPostForm.getIntroduction()
                , blogPostForm.isBlogPostPrivate());

        blogPostService.createPost(currentAccountId, blogPostCreationDto);
        Account currentAccount = accountRepository.findById(currentAccountId).orElseThrow();
        redirectAttributes.addAttribute("username", currentAccount.getUsername());
        redirectAttributes.addFlashAttribute("isNewPostCreated", true);
        return "redirect:/@{username}";
    }
}