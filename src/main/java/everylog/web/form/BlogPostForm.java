package everylog.web.form;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class BlogPostForm {

    @NotBlank
    @Length(max = 150)
    private String title;

    private String content;

}
