package com.example.management_task.controller;

import com.example.management_task.dto.comments_dto.CommentsInputDto;
import com.example.management_task.dto.comments_dto.CommentsUpdateDto;
import com.example.management_task.model.CommentsModel;
import com.example.management_task.model.TaskModel;
import com.example.management_task.service.CommentsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/v1/comments")
public class CommentsController {
    private final CommentsService commentsService;

    @Operation(summary = "Create a new comment",
            description = "This API is used to create a new comment for a task.")
    @PostMapping("/create")
    public ResponseEntity<TaskModel> addComment(@Valid @RequestBody CommentsInputDto commentsInputDto, Principal principal) {
        return new ResponseEntity<>(commentsService.save(commentsInputDto, principal), HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing comment",
            description = "This API is used to update an existing comment.")
    @PutMapping("/update")
    public ResponseEntity<TaskModel> updateComment(@Valid @RequestBody CommentsUpdateDto commentsInputDto, Principal principal) {
        return new ResponseEntity<>(commentsService.updateComment(commentsInputDto, principal), HttpStatus.CREATED);
    }

    @Operation(summary = "Get all comments for a task",
            description = "This API is used to get all comments for a specific task.")
    @GetMapping("/all-task-comments/{id}")
    public ResponseEntity<List<CommentsModel>> getAllCommentsByTaskId(@PathVariable Long id) {
        return new ResponseEntity<>(commentsService.getTaskComments(id), HttpStatus.OK);
    }

    @Operation(summary = "Get all comments created by the authenticated user",
            description = "This API is used to get all comments created by the authenticated user.")
    @GetMapping("/all-my-comments")
    public ResponseEntity<List<CommentsModel>> getAllMyComments(Principal principal) {
        return new ResponseEntity<>(commentsService.getMyComments(principal), HttpStatus.OK);
    }

    @Operation(summary = "Delete a comment by its creator",
            description = "This API is used to delete a comment created by the authenticated user.")
    @DeleteMapping("/delete-by-comment-creator/{commentId}")
    public ResponseEntity<String> deleteByCommentAuthor(@PathVariable Long commentId, Principal principal) {
        String msg = String.format("msg by id : %s was successfully deleted", commentId);
        commentsService.deleteByCommentAuthor(commentId, principal);
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

}
