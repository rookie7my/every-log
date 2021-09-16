package everylog.domain.comment.controller;

import everylog.domain.account.controller.CurrentAccountId;
import everylog.domain.comment.domain.Comment;
import everylog.domain.comment.service.CommentService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/api/comments")
    public ResponseEntity<CommentsQueryResponseDto> queryComments(
            @ModelAttribute CommentsQueryRequestDto commentsQueryRequestDto) {

        List<Comment> comments = commentService.findAllByBlogPost(commentsQueryRequestDto.getBlogPostId());
        List<CommentDto> commentDtos = comments.stream()
                .map(CommentDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new CommentsQueryResponseDto(commentDtos));
    }

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

    @Getter
    @Setter
    private static class CommentsQueryRequestDto {
        private Long blogPostId;
    }

    @Getter
    private static class CommentsQueryResponseDto {
        private List<CommentDto> comments;

        public CommentsQueryResponseDto(List<CommentDto> comments) {
            this.comments = comments;
        }
    }
}
