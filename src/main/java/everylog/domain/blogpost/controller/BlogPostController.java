package everylog.domain.blogpost.controller;

import everylog.domain.account.domain.Account;
import everylog.domain.blogpost.domain.BlogPost;
import everylog.domain.account.repository.AccountRepository;
import everylog.domain.blogpost.service.BlogPostCreationDto;
import everylog.domain.blogpost.service.BlogPostService;
import everylog.domain.account.controller.CurrentAccountId;
import everylog.domain.blogpost.controller.form.BlogPostForm;
import everylog.domain.blogpost.controller.form.BlogPostUpdateForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/@{username}/blog-posts/{blogPostId}/{blogPostTitle}")
    public String getBlogPost(@CurrentAccountId Long currentAccountId,
                              @PathVariable String username,
                              @PathVariable Long blogPostId,
                              @PathVariable String blogPostTitle,
                              Model model) {

        BlogPost blogPost = blogPostService.findBlogPost(blogPostId, blogPostTitle, username);

        boolean isCurrentAccountWriter = false;
        if(currentAccountId != null) {
            Account currentAccount = accountRepository.findById(currentAccountId).orElseThrow();
            isCurrentAccountWriter = blogPost.matchWriter(currentAccount);
        }

        model.addAttribute("blogPost", blogPost);
        model.addAttribute("isCurrentAccountWriter", isCurrentAccountWriter);
        return "blogpost/detail";
    }

    @GetMapping("/@{username}/blog-posts/{blogPostId}/edit")
    public String getBlogPostEditForm(@CurrentAccountId Long currentAccountId,
                                      @PathVariable String username,
                                      @PathVariable Long blogPostId,
                                      Model model) {
        BlogPost blogPost = blogPostService.findBlogPost(blogPostId, username);
        model.addAttribute(new BlogPostUpdateForm(blogPost));
        return "blogpost/update-form";
    }

    @PostMapping("/@{username}/blog-posts/{blogPostId}/edit")
    public String updateBlogPost(@CurrentAccountId Long currentAccountId,
                                 @PathVariable String username, @PathVariable Long blogPostId,
                                 @ModelAttribute @Valid BlogPostUpdateForm blogPostUpdateForm,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {
        BlogPost blogPost = blogPostService.findBlogPost(blogPostId, username);

        if(bindingResult.hasErrors()) {
            return "blogpost/update-form";
        }

        blogPostService.updateBlogPost(blogPostId, blogPostUpdateForm.getTitle(), blogPostUpdateForm.getContent());

        redirectAttributes.addAttribute("username", username);
        redirectAttributes.addAttribute("blogPostId", blogPostId);
        redirectAttributes.addAttribute("blogPostTitle", blogPost.getTitle());
        return "redirect:/@{username}/blog-posts/{blogPostId}/{blogPostTitle}";
    }
}
