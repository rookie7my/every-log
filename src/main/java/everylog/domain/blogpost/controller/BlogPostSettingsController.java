package everylog.domain.blogpost.controller;

import everylog.domain.account.controller.CurrentAccountId;
import everylog.domain.blogpost.controller.form.BlogPostSettingsForm;
import everylog.domain.blogpost.domain.BlogPost;
import everylog.domain.blogpost.service.BlogPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class BlogPostSettingsController {

    private final BlogPostService blogPostService;

    @GetMapping("/@{username}/blog-posts/{blogPostId}/settings")
    public String getBlogPostSettingsPage(@CurrentAccountId Long currentAccountId,
                                          @PathVariable String username, @PathVariable Long blogPostId, Model model) {
        BlogPost blogPost = blogPostService.findBlogPostForUpdate(blogPostId, username, currentAccountId);
        model.addAttribute(blogPost);
        model.addAttribute(new BlogPostSettingsForm(blogPost.getIntroduction(), blogPost.isBlogPostPrivate()));
        return "blogpost/settings-form";
    }

    @PostMapping("@{username}/blog-posts/{blogPostId}/settings")
    public String updateBlogPostSettings(@CurrentAccountId Long currentAccountId, @PathVariable String username,
                                         @PathVariable Long blogPostId, Model model,
                                         @ModelAttribute BlogPostSettingsForm blogPostSettingsForm,
                                         BindingResult bindingResult,
                                         RedirectAttributes redirectAttributes) {
        BlogPost blogPost = blogPostService.findBlogPostForUpdate(blogPostId, username, currentAccountId);
        if(bindingResult.hasErrors()) {
            model.addAttribute(blogPost);
            return "blogpost/settings-form";
        }

        blogPostService.updateBlogPostSettings(blogPostId,
                blogPostSettingsForm.getIntroduction(), blogPostSettingsForm.isBlogPostPrivate());

        redirectAttributes.addAttribute("username", username);
        redirectAttributes.addAttribute("blogPostId", blogPostId);
        redirectAttributes.addAttribute("blogPostTitle", blogPost.getTitle());
        return "redirect:/@{username}/blog-posts/{blogPostId}/{blogPostTitle}";
    }
}
