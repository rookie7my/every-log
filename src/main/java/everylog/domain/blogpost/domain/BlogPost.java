package everylog.domain.blogpost.domain;

import everylog.domain.account.domain.Account;
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
public class BlogPost {

    @Id
    @GeneratedValue
    @Column(name = "blog_post_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob
    private String content;

    @Column(nullable = false)
    private String introduction;

    @Column(nullable = false)
    private boolean blogPostPrivate;

    @Column(nullable = false)
    private LocalDateTime createdDateTime;

    @Column(nullable = false)
    private LocalDateTime updatedDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private Account writer;

    public BlogPost(String title, String content, String introduction, boolean blogPostPrivate, Account writer) {
        this.title = title;
        this.content = content;
        this.introduction = introduction;
        this.blogPostPrivate = blogPostPrivate;
        this.writer = writer;
        this.createdDateTime = this.updatedDateTime = LocalDateTime.now();
    }

    public boolean matchTitle(String blogPostTitle) {
        return this.title.equals(blogPostTitle);
    }

    public boolean matchWriter(Account writer) {
        return this.writer.equals(writer);
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
        this.updatedDateTime = LocalDateTime.now();
    }

    public void updateSettings(String introduction, boolean blogPostPrivate) {
        this.introduction = introduction;
        this.blogPostPrivate = blogPostPrivate;
    }
}
