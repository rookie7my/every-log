package everylog.domain.account.controller;

import everylog.domain.account.domain.Account;
import everylog.domain.account.exception.AccountNotFoundException;
import everylog.domain.account.repository.AccountRepository;
import everylog.domain.blogpost.controller.RequestedPageNumber;
import everylog.domain.blogpost.domain.BlogPost;
import everylog.domain.blogpost.service.BlogPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AccountHomeController {

    private final AccountRepository accountRepository;
    private final BlogPostService blogPostService;

    @GetMapping("/@{username}")
    public String getAccountHome(@CurrentAccountId Long currentAccountId,
                                 @PathVariable String username, Model model,
                                 @ModelAttribute RequestedPageNumber requestedPageNumber,
                                 RedirectAttributes redirectAttributes) {

        Account blogHomeOwner = accountRepository.findByUsername(username);
        if(blogHomeOwner == null) {
            throw new AccountNotFoundException("An account with given username does not exist.");
        }

        if(!requestedPageNumber.isPageValid()) {
            redirectAttributes.addAttribute("username", blogHomeOwner.getUsername());
            return "redirect:/@{username}";
        }

        int zeroIndexedPageNumber = requestedPageNumber.getZeroIndexedPageNumber();
        Page<BlogPost> blogPostPage = blogPostService.findPageOfBlogPost(blogHomeOwner, false, zeroIndexedPageNumber);

        if(blogPostPage.getTotalPages() != 0 && (zeroIndexedPageNumber >= blogPostPage.getTotalPages())) {
            int largestOneIndexedPageNum = blogPostPage.getTotalPages();
            redirectAttributes.addAttribute("username", blogHomeOwner.getUsername());
            redirectAttributes.addAttribute("page", largestOneIndexedPageNum);
            return "redirect:/@{username}";
        }

        boolean isCurrentAccountBlogHomeOwner = false;
        if(currentAccountId != null) {
            Account currentAccount = accountRepository.findById(currentAccountId)
                    .orElseThrow(() -> new IllegalArgumentException("currentAccountId is not valid."));
            isCurrentAccountBlogHomeOwner = currentAccount.matchUsername(username);
        }

        model.addAttribute("account", blogHomeOwner);
        model.addAttribute("blogPostPage", blogPostPage);
        model.addAttribute("isCurrentAccountBlogHomeOwner", isCurrentAccountBlogHomeOwner);
        return "account/home";
    }

    @GetMapping("/@{username}/about")
    public String getAccountIntroductionPage(@CurrentAccountId Long currentAccountId,
                                             @PathVariable String username, Model model) {

        Account blogHomeOwner = accountRepository.findByUsername(username);
        if(blogHomeOwner == null) {
            throw new AccountNotFoundException("An account with given username does not exist.");
        }

        boolean isCurrentAccountBlogHomeOwner = false;
        if(currentAccountId != null) {
            Account currentAccount = accountRepository.findById(currentAccountId)
                    .orElseThrow(() -> new IllegalArgumentException("currentAccountId is not valid."));
            isCurrentAccountBlogHomeOwner = currentAccount.matchUsername(username);
        }

        model.addAttribute("account", blogHomeOwner);
        model.addAttribute("isCurrentAccountBlogHomeOwner", isCurrentAccountBlogHomeOwner);
        return "account/about";
    }

    @GetMapping("/private-blog-posts")
    public String getPrivateBlogPostPage(@CurrentAccountId Long currentAccountId, Model model,
                                         @ModelAttribute RequestedPageNumber requestedPageNumber,
                                         RedirectAttributes redirectAttributes) {

        Account currentAccount = accountRepository.findById(currentAccountId)
                .orElseThrow(() -> new IllegalArgumentException("currentAccountId is not valid."));

        if(!requestedPageNumber.isPageValid()) {
            return "redirect:/private-blog-posts";
        }

        int zeroIndexedPageNumber = requestedPageNumber.getZeroIndexedPageNumber();
        Page<BlogPost> blogPostPage = blogPostService.findPageOfBlogPost(currentAccount, true, zeroIndexedPageNumber);

        if(blogPostPage.getTotalPages() != 0 && (zeroIndexedPageNumber >= blogPostPage.getTotalPages())) {
            int largestOneIndexedPageNum = blogPostPage.getTotalPages();
            redirectAttributes.addAttribute("page", largestOneIndexedPageNum);
            return "redirect:/private-blog-posts";
        }

        model.addAttribute("account", currentAccount);
        model.addAttribute("blogPostPage", blogPostPage);
        model.addAttribute("isCurrentAccountBlogHomeOwner", true);

        return "account/private-blog-posts";
    }
}