package everylog.web;

import everylog.domain.Account;
import everylog.domain.BlogPost;
import everylog.repository.AccountRepository;
import everylog.repository.BlogPostRepository;
import everylog.service.BlogPostCreationDto;
import everylog.service.BlogPostService;
import everylog.web.form.BlogPostForm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/@{username}/{blogPostId}/{blogPostTitle}")
    public String getBlogPost(@PathVariable String username,
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

        model.addAttribute("blogPost", blogPost);
        return "blogpost/detail";
    }
}
