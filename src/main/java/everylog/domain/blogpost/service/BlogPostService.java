package everylog.domain.blogpost.service;

import everylog.domain.account.domain.Account;
import everylog.domain.account.exception.AccountNotFoundException;
import everylog.domain.account.repository.AccountRepository;
import everylog.domain.blogpost.domain.BlogPost;
import everylog.domain.blogpost.exception.BlogPostNotFoundException;
import everylog.domain.blogpost.repository.BlogPostRepository;
import everylog.global.error.exception.ErrorResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BlogPostService {

    private final AccountRepository accountRepository;
    private final BlogPostRepository blogPostRepository;

    public static final int BLOG_PAGE_SIZE = 3;

    @Transactional
    public Long createPost(Long writerId, BlogPostCreationDto blogPostCreationDto) {
        Account writer = accountRepository.findById(writerId).orElseThrow();
        BlogPost post = new BlogPost(blogPostCreationDto.getTitle()
                , blogPostCreationDto.getContent()
                , blogPostCreationDto.getIntroduction()
                , blogPostCreationDto.isBlogPostPrivate()
                , writer);
        BlogPost savedPost = blogPostRepository.save(post);
        return savedPost.getId();
    }

    @Transactional(readOnly = true)
    public BlogPost findBlogPost(Long blogPostId, String writerName) {
        BlogPost blogPost = blogPostRepository.findByIdWithWriter(blogPostId);

        if(blogPost == null) {
            throw new BlogPostNotFoundException(ErrorResult.BLOG_POST_NOT_FOUND);
        }

        if(!blogPost.getWriter().matchUsername(writerName)) {
            throw new BlogPostNotFoundException(ErrorResult.BLOG_POST_NOT_FOUND);
        }

        return blogPost;
    }

    @Transactional(readOnly = true)
    public BlogPost findBlogPost(Long blogPostId, String blogPostTitle, String writerName) {
        BlogPost blogPost = findBlogPost(blogPostId, writerName);

        if(!blogPost.matchTitle(blogPostTitle)) {
            throw new BlogPostNotFoundException(ErrorResult.BLOG_POST_NOT_FOUND);
        }
        return blogPost;
    }

    @Transactional(readOnly = true)
    public BlogPost findBlogPostForUpdate(Long blogPostId, String username, Long currentAccountId) {
        BlogPost blogPost = findBlogPost(blogPostId, username);

        Account currentAccount = accountRepository.findById(currentAccountId)
                .orElseThrow(() -> new AccountNotFoundException(ErrorResult.INVALID_CURRENT_ACCOUNT_ID));

        if(!blogPost.matchWriter(currentAccount)) {
            throw new AccessDeniedException("Only writer can update blogPost");
        }
        return blogPost;
    }

    @Transactional
    public void updateBlogPost(Long blogPostId, String title, String content) {
        BlogPost blogPost = blogPostRepository.findById(blogPostId)
                .orElseThrow(()-> new IllegalArgumentException("blogPostId is not valid."));
        blogPost.update(title, content);
    }

    @Transactional(readOnly = true)
    public Page<BlogPost> findPageOfBlogPost(Account writer, boolean blogPostPrivate ,int pageNumber) {
        Sort descendingByBlogPostId = Sort.sort(BlogPost.class).by(BlogPost::getId).descending();
        PageRequest pageRequest = PageRequest.of(pageNumber, BLOG_PAGE_SIZE, descendingByBlogPostId);
        return blogPostRepository.findPageOfBlogPost(writer, blogPostPrivate, pageRequest);
    }

    @Transactional
    public void updateBlogPostSettings(Long blogPostId, String introduction, boolean blogPostPrivate) {
        BlogPost blogPost = blogPostRepository.findById(blogPostId)
                .orElseThrow(()-> new IllegalArgumentException("blogPostId is not valid."));
        blogPost.updateSettings(introduction, blogPostPrivate);
    }
}
