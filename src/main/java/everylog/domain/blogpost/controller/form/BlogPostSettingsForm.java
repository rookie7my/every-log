package everylog.domain.blogpost.controller.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlogPostSettingsForm {

    @NotBlank
    @Length(max = 150)
    private String introduction;

    private boolean blogPostPrivate;
}
