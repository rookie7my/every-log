package everylog.domain.comment.controller;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(force = true)
public class CommentQueryResponseDto {
    private final int count;
    private final List<CommentResponseDto> comments;

    public CommentQueryResponseDto(List<CommentResponseDto> commentResponseDtos) {
        this.comments = commentResponseDtos;
        this.count = commentResponseDtos.size();
    }
}
