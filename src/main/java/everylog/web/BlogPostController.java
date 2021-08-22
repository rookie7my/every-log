package everylog.web;

import everylog.domain.Account;
import everylog.domain.BlogPost;
import everylog.repository.AccountRepository;
import everylog.repository.BlogPostRepository;
import everylog.service.BlogPostCreationDto;
import everylog.service.BlogPostService;
import everylog.web.form.BlogPostForm;
import everylog.web.form.BlogPostUpdateForm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class BlogPostController {

    private final AccountRepository accountRepository;
    private final BlogPostService blogPostService;
    private final BlogPostRepository blogPostRepository;

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

        BlogPost blogPost = blogPostRepository.findByIdWithAccount(blogPostId);

        if(blogPost == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "exist",
                    new IllegalArgumentException("A blogPost with given blogPostId does not exist.")
            );
        }

        if(!blogPost.matchTitle(blogPostTitle) || !blogPost.getAccount().matchUsername(username)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "exist",
                    new IllegalArgumentException("There is no blogPost with given blogPostId, blogPostTitle, writer name")
            );
        }

        boolean isCurrentAccountOwner = false;
        if(currentAccountId != null) {
            Account currentAccount = accountRepository.findById(currentAccountId).orElseThrow();
            isCurrentAccountOwner = blogPost.matchAccount(currentAccount);
        }

        model.addAttribute("blogPost", blogPost);
        model.addAttribute("isCurrentAccountOwner", isCurrentAccountOwner);
        return "blogpost/detail";
    }

    @GetMapping("/@{username}/blog-posts/{blogPostId}/edit")
    public String getBlogPostEditForm(@CurrentAccountId Long currentAccountId,
                                      @PathVariable String username,
                                      @PathVariable Long blogPostId,
                                      Model model) {

        BlogPost blogPost = blogPostRepository.findByIdWithAccount(blogPostId);

        if(blogPost == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "exist",
                    new IllegalArgumentException("A blogPost with given blogPostId does not exist.")
            );
        }

        if(!blogPost.getAccount().matchUsername(username)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "exist",
                    new IllegalArgumentException("There is no blogPost with given blogPostId, writer name")
            );
        }

        Account currentAccount = accountRepository.findById(currentAccountId).orElseThrow();
        if(!blogPost.matchAccount(currentAccount)) {
            throw new AccessDeniedException("Only Writer can edit the blog post");
        }

        model.addAttribute(new BlogPostUpdateForm(blogPost));
        return "blogpost/update-form";
    }
}
