package everylog.domain.comment.domain;

import everylog.domain.account.domain.Account;
import everylog.domain.blogpost.domain.BlogPost;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime createdDateTime;

    @Column(nullable = false)
    private LocalDateTime updatedDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private Account writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blog_post_id")
    private BlogPost blogPost;

    public Comment(String content, Account writer, BlogPost blogPost) {
        this.content = content;
        this.writer = writer;
        this.blogPost = blogPost;

        LocalDateTime now = LocalDateTime.now();
        this.createdDateTime = now;
        this.updatedDateTime = now;
    }
}
