package everylog.domain.blogpost.controller.form;

import everylog.domain.blogpost.domain.BlogPost;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BlogPostUpdateForm {

    private String title;

    private String content;

    public BlogPostUpdateForm(BlogPost blogPost) {
        this.title = blogPost.getTitle();
        this.content = blogPost.getContent();
    }
}
