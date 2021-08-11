package everylog.service;

import everylog.domain.Account;
import everylog.domain.BlogPost;
import everylog.repository.AccountRepository;
import everylog.repository.BlogPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BlogPostService {

    private final AccountRepository accountRepository;
    private final BlogPostRepository blogPostRepository;

    @Transactional
    public Long createPost(Long writerId, BlogPostCreationDto blogPostCreationDto) {
        Account writer = accountRepository.findById(writerId).orElseThrow();
        BlogPost post = new BlogPost(blogPostCreationDto.getTitle(), blogPostCreationDto.getContent(), writer);
        BlogPost savedPost = blogPostRepository.save(post);
        return savedPost.getId();
    }
}
