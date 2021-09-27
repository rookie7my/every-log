package everylog.domain.comment.controller;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class CommentCreationRequestDto {

    @NotNull
    private final Long blogPostId;

    @NotEmpty
    private final String content;
}
