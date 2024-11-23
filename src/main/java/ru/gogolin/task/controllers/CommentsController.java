package ru.gogolin.task.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.gogolin.task.dtos.CommentDto;
import ru.gogolin.task.dtos.CommentResponseDto;
import ru.gogolin.task.services.CommentService;
import java.security.Principal;
import java.util.List;

@Tag(name = "Comment Controller", description = "API for working with comments on tasks.")
@RestController("/comment")
public class CommentsController {

    private final CommentService commentService;

    public CommentsController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(summary = "Creation comment.")
    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(@RequestBody CommentDto commentDto, Authentication authentication) {
        return new ResponseEntity<>(commentService.addComment(commentDto, authentication), HttpStatus.CREATED);
    }

    @Operation(summary = "Deletion comment by ID.")
    @DeleteMapping
    public ResponseEntity<String> deleteComment(@RequestBody CommentDto commentDto, Authentication authentication) {
        commentService.deleteComment(commentDto, authentication);
        return ResponseEntity.ok("Comment deleted");
    }

    @Operation(summary = "Getting comments by title of task")
    @GetMapping("/get")
    public ResponseEntity<List<CommentResponseDto>> getCommentsByTitle(@RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "10") int size,
                                                                       @RequestParam(name = "title") String taskTitle, Principal principal) {
        return ResponseEntity.ok(commentService.getComments(taskTitle, principal, page, size));
    }
}
