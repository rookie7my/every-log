package everylog.domain.blogpost.service;

import everylog.domain.account.domain.Account;
import everylog.domain.blogpost.domain.BlogPost;
import everylog.domain.account.repository.AccountRepository;
import everylog.domain.blogpost.exception.BlogPostNotFoundException;
import everylog.domain.blogpost.repository.BlogPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
            throw new BlogPostNotFoundException("A blogPost with given blogPostId does not exist.");
        }

        if(!blogPost.getWriter().matchUsername(writerName)) {
            throw new BlogPostNotFoundException("There is no blogPost with given blogPostId, writerName");
        }

        return blogPost;
    }

    @Transactional(readOnly = true)
    public BlogPost findBlogPost(Long blogPostId, String blogPostTitle, String writerName) {
        BlogPost blogPost = findBlogPost(blogPostId, writerName);

        if(!blogPost.matchTitle(blogPostTitle)) {
            throw new BlogPostNotFoundException("There is no blogPost with given blogPostId, blogPostTitle, writerName");
        }
        return blogPost;
    }

    @Transactional
    public void updateBlogPost(Long blogPostId, String title, String content) {
        BlogPost blogPost = blogPostRepository.findById(blogPostId).orElseThrow(BlogPostNotFoundException::new);
        blogPost.update(title, content);
    }

    @Transactional(readOnly = true)
    public Page<BlogPost> findPageOfBlogPost(Account writer, boolean blogPostPrivate ,int pageNumber) {
        Sort descendingByBlogPostId = Sort.sort(BlogPost.class).by(BlogPost::getId).descending();
        PageRequest pageRequest = PageRequest.of(pageNumber, BLOG_PAGE_SIZE, descendingByBlogPostId);
        return blogPostRepository.findPageOfBlogPost(writer, blogPostPrivate, pageRequest);
    }
}
