package everylog.domain.comment.controller;

import everylog.domain.comment.domain.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentCreationResponseDto {
    private final String writerName;
    private final LocalDateTime createdDateTime;
    private final String content;

    public CommentCreationResponseDto(Comment comment) {
        this.writerName = comment.getWriter().getUsername();
        this.createdDateTime = comment.getCreatedDateTime();
        this.content = comment.getContent();
    }
}
