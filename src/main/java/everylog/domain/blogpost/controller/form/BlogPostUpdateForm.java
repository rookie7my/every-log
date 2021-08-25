package everylog.domain.blogpost.controller.form;

import everylog.domain.blogpost.domain.BlogPost;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class BlogPostUpdateForm {

    @NotBlank
    @Length(max = 150)
    private String title;

    private String content;

    public BlogPostUpdateForm(BlogPost blogPost) {
        this.title = blogPost.getTitle();
        this.content = blogPost.getContent();
    }
}
