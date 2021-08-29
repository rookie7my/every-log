package everylog.domain.blogpost.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BlogPostCreationDto {

    private String title;

    private String content;

    private String introduction;

    private boolean blogPostPrivate;
}
