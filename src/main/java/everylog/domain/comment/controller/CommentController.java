package everylog.domain.comment.controller;

import everylog.domain.account.controller.CurrentAccountId;
import everylog.domain.comment.domain.Comment;
import everylog.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/api/comments")
    public ResponseEntity<CommentCreationResponseDto> createComment(@CurrentAccountId Long currentAccountId,
                                              @RequestBody @Valid CommentCreationRequestDto commentCreationRequestDto) {
        if(currentAccountId == null) {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setLocation(URI.create("/login"));
            return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
        }

        Comment createdComment = commentService.createComment(currentAccountId,
                commentCreationRequestDto.getBlogPostId(),
                commentCreationRequestDto.getContent());
        return ResponseEntity.created(URI.create("/api/comments" + "/" + createdComment.getId()))
                .body(new CommentCreationResponseDto(createdComment));
    }
}
