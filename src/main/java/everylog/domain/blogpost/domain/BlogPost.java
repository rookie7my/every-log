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
    private LocalDateTime createdDateTime;

    @Column(nullable = false)
    private LocalDateTime updatedDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    public BlogPost(String title, String content, Account account) {
        this.title = title;
        this.content = content;
        this.account = account;
        this.createdDateTime = this.updatedDateTime = LocalDateTime.now();
    }

    public boolean matchTitle(String blogPostTitle) {
        return this.title.equals(blogPostTitle);
    }

    public boolean matchAccount(Account currentAccount) {
        return this.account.equals(currentAccount);
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
        this.updatedDateTime = LocalDateTime.now();
    }
}
