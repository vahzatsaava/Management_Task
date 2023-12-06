package com.example.management_task.controller;

import com.example.management_task.dto.comments_dto.CommentsInputDto;
import com.example.management_task.dto.comments_dto.CommentsUpdateDto;
import com.example.management_task.model.CommentsModel;
import com.example.management_task.model.TaskModel;
import com.example.management_task.service.CommentsService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/v1/comments")
public class CommentsController {
    private final CommentsService commentsService;

    @PostMapping("/create")
    public ResponseEntity<TaskModel> addComment(@Valid @RequestBody CommentsInputDto commentsInputDto) {
        return new ResponseEntity<>(commentsService.save(commentsInputDto), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<TaskModel> updateComment(@Valid @RequestBody CommentsUpdateDto commentsInputDto) {
        return new ResponseEntity<>(commentsService.updateComment(commentsInputDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable Long id) {
        String msg = String.format("msg by id : %s was successfully deleted", id);
        commentsService.delete(id);
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @GetMapping("/all-task-comments/{id}")
    public ResponseEntity<List<CommentsModel>> getAllCommentsByTaskId(@PathVariable Long id) {
        return new ResponseEntity<>(commentsService.getTaskComments(id), HttpStatus.OK);
    }

}
