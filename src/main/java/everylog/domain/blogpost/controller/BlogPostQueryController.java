package everylog.domain.blogpost.controller;

import everylog.domain.account.controller.CurrentAccountId;
import everylog.domain.account.domain.Account;
import everylog.domain.account.repository.AccountRepository;
import everylog.domain.blogpost.domain.BlogPost;
import everylog.domain.blogpost.service.BlogPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class BlogPostQueryController {

    private final BlogPostService blogPostService;
    private final AccountRepository accountRepository;

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

        if(blogPost.isBlogPostPrivate() && !isCurrentAccountWriter) {
            throw new AccessDeniedException("only writer can access private blog post.");
        }

        model.addAttribute("blogPost", blogPost);
        model.addAttribute("isCurrentAccountWriter", isCurrentAccountWriter);
        return "blogpost/detail";
    }
}