package everylog.domain.account.controller;

import everylog.domain.account.domain.Account;
import everylog.domain.account.exception.AccountNotFoundException;
import everylog.domain.account.repository.AccountRepository;
import everylog.domain.blogpost.controller.RequestedPageNumber;
import everylog.domain.blogpost.domain.BlogPost;
import everylog.domain.blogpost.service.BlogPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AccountHomeController {

    private final AccountRepository accountRepository;
    private final BlogPostService blogPostService;

    @GetMapping("/@{username}")
    public String getAccountHome(@PathVariable String username, Model model,
                                 @ModelAttribute RequestedPageNumber requestPageNumber,
                                 RedirectAttributes redirectAttributes) {

        Account homeOwner = accountRepository.findByUsername(username);
        if(homeOwner == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "exist",
                    new IllegalArgumentException("An account with given username does not exist.")
            );
        }

        if(!requestPageNumber.isPageValid()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "exist",
                    new IllegalArgumentException("A page with given requested page number dose not exist.")
            );
        }

        int zeroIndexedPageNumber = requestPageNumber.getZeroIndexedPageNumber();
        Page<BlogPost> blogPostPage = blogPostService.findPageOfPublicBlogPostByWriter(homeOwner, zeroIndexedPageNumber);

        if(blogPostPage.getTotalPages() != 0 && (zeroIndexedPageNumber >= blogPostPage.getTotalPages())) {
            int largestOneIndexedPageNum = blogPostPage.getTotalPages();
            redirectAttributes.addAttribute("username", homeOwner.getUsername());
            redirectAttributes.addAttribute("page", largestOneIndexedPageNum);
            return "redirect:/@{username}";
        }

        model.addAttribute("homeOwner", homeOwner);
        model.addAttribute("blogPostPage", blogPostPage);
        return "account/home";
    }

    @GetMapping("/@{username}/about")
    public String getAccountIntroductionPage(@PathVariable String username, Model model) {
        Account account = accountRepository.findByUsername(username);
        if(account == null) {
            throw new AccountNotFoundException("An account with given username does not exist.");
        }
        model.addAttribute("homeOwner", account);
        return "account/about";
    }
}
