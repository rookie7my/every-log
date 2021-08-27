package everylog.domain.blogpost.controller.interceptor;

import everylog.domain.account.domain.Account;
import everylog.domain.account.repository.AccountRepository;
import everylog.domain.account.service.AccountUserDetails;
import everylog.domain.blogpost.domain.BlogPost;
import everylog.domain.blogpost.service.BlogPostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class BlogPostEditAuthorizationCheckInterceptor implements HandlerInterceptor {

    private final BlogPostService blogPostService;
    private final AccountRepository accountRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        log.info("blogPostEditAuthorizationCheckInterceptor works");

        Long currentAccountId = getCurrentAccountId();
        String username = getUsername(request);
        Long blogPostId = getBlogPostId(request);

        BlogPost blogPost = blogPostService.findBlogPost(blogPostId, username);
        Account currentAccount = accountRepository.findById(currentAccountId).orElseThrow();

        if (!blogPost.matchWriter(currentAccount)) {
            throw new AccessDeniedException("Only Writer can edit the blog post");
        }

        return true;
    }

    private Long getCurrentAccountId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AccountUserDetails principal = (AccountUserDetails) authentication.getPrincipal();
        return principal.getId();
    }

    private String getPathVariable(HttpServletRequest request, String pathVariableName) {
        Map<String, String> pathVariableMap = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        return pathVariableMap.get(pathVariableName);
    }

    private String getUsername(HttpServletRequest request) {
        return getPathVariable(request, "username");
    }

    private Long getBlogPostId(HttpServletRequest request) {
        String strBlogPostId = getPathVariable(request, "blogPostId");
        return Long.valueOf(strBlogPostId);
    }
}
