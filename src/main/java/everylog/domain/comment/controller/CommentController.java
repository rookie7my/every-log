package everylog.domain.comment.controller;

import everylog.domain.account.controller.CurrentAccountId;
import everylog.domain.comment.domain.Comment;
import everylog.domain.comment.repository.CommentRepository;
import everylog.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final CommentRepository commentRepository;

    @GetMapping("/api/comments")
    public CommentQueryResponseDto searchForComments(@RequestParam Long blogPostId) {
        List<Comment> comments = commentRepository.findAllByBlogPostId(blogPostId);
        List<CommentResponseDto> commentResponseDtos = comments.stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
        return new CommentQueryResponseDto(commentResponseDtos);
    }

    @PostMapping("/api/comments")
    public ResponseEntity<CommentCreationResponseDto> createComment(@CurrentAccountId Long currentAccountId,
                                              @RequestBody @Valid CommentCreationRequestDto commentCreationRequestDto) {
        Comment createdComment = commentService.createComment(currentAccountId,
                commentCreationRequestDto.getBlogPostId(),
                commentCreationRequestDto.getContent());
        return ResponseEntity.created(URI.create("/api/comments" + "/" + createdComment.getId()))
                .body(new CommentCreationResponseDto(createdComment));
    }
}
