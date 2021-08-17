package everylog.web;

import everylog.domain.BlogPost;
import everylog.repository.BlogPostRepository;
import everylog.web.form.SignUpForm;
import everylog.web.validator.SignUpFormValidator;
import everylog.domain.Account;
import everylog.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final AccountRepository accountRepository;
    private final SignUpFormValidator signUpFormValidator;
    private final PasswordEncoder passwordEncoder;
    private final BlogPostRepository blogPostRepository;

    public static final int BLOG_PAGE_SIZE = 3;

    @InitBinder("signUpForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signUpFormValidator);
    }

    @GetMapping("/@{username}")
    public String accountHome(@PathVariable String username, Model model,
                              @ModelAttribute @Valid RequestedPageNumber requestPageNumber,
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
        Sort descendingByBlogPostId = Sort.sort(BlogPost.class).by(BlogPost::getId).descending();
        PageRequest pageRequest = PageRequest.of(zeroIndexedPageNumber, BLOG_PAGE_SIZE, descendingByBlogPostId);
        Page<BlogPost> blogPostPage = blogPostRepository.findByAccount(homeOwner, pageRequest);

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

    @GetMapping("/sign-up")
    public String signUpForm(Model model) {
        model.addAttribute(new SignUpForm());
        return "account/sign-up-form";
    }

    @PostMapping("/sign-up")
    public String processSignUp(@ModelAttribute @Valid SignUpForm signUpForm,
                                Errors errors) {
        if(errors.hasErrors()) {
            return "account/sign-up-form";
        }

        Account account = new Account(
                signUpForm.getUsername(),
                signUpForm.getEmail(),
                passwordEncoder.encode(signUpForm.getPassword())
        );

        accountRepository.save(account);
        return "redirect:/";
    }
}
