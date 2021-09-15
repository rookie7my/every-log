package everylog.domain.comment.controller;

import everylog.domain.account.controller.CurrentAccountId;
import everylog.domain.comment.domain.Comment;
import everylog.domain.comment.service.CommentService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/api/comments")
    public ResponseEntity<CommentDto> createComment(@CurrentAccountId Long currentAccountId,
                                                    @RequestBody CommentCreationRequestDto commentCreationRequestDto) {
        Long createdCommentId = commentService.createComment(currentAccountId,
                                                             commentCreationRequestDto.getBlogPostId(),
                                                             commentCreationRequestDto.getContent());
        Comment comment = commentService.findById(createdCommentId);
        return ResponseEntity.ok(new CommentDto(comment));
    }

    @Getter
    @NoArgsConstructor
    private static class CommentCreationRequestDto {
        private Long blogPostId;
        private String content;
    }

    @Getter
    private static class CommentDto {
        private Long id;
        private String writerName;
        private LocalDateTime createdDateTime;
        private String content;

        CommentDto(Comment comment) {
            id = comment.getId();
            writerName = comment.getWriter().getUsername();
            createdDateTime = comment.getCreatedDateTime();
            content = comment.getContent();
        }
    }
}
