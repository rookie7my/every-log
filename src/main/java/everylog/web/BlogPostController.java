package everylog.web;

import everylog.domain.Account;
import everylog.repository.AccountRepository;
import everylog.service.BlogPostCreationDto;
import everylog.service.BlogPostService;
import everylog.web.form.BlogPostForm;
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
public class BlogPostController {

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

        BlogPostCreationDto blogPostCreationDto = new BlogPostCreationDto(blogPostForm.getTitle(), blogPostForm.getContent());
        blogPostService.createPost(currentAccountId, blogPostCreationDto);
        Account currentAccount = accountRepository.findById(currentAccountId).orElseThrow();
        redirectAttributes.addAttribute("username", currentAccount.getUsername());
        redirectAttributes.addFlashAttribute("isNewPostCreated", true);
        return "redirect:/@{username}";
    }
}
