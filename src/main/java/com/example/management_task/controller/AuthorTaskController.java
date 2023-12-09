package com.example.management_task.controller;

import com.example.management_task.dto.tasks_dto.TaskCreateInputDto;
import com.example.management_task.dto.tasks_dto.TaskUpdateInputDto;
import com.example.management_task.model.TaskModel;
import com.example.management_task.service.AuthorTaskService;
import io.swagger.annotations.ApiParam;
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
@RequestMapping("/api/v1/authors/tasks")
public class AuthorTaskController {
    private final AuthorTaskService authorTaskService;

    @Operation(summary = "[Author] Create a new task",
            description = "This API is used by authors to create a new task.")
    @PostMapping("/create")
    public ResponseEntity<TaskModel> createTask(@Valid @RequestBody TaskCreateInputDto taskCreateInputDto, Principal principal) {
        return new ResponseEntity<>(authorTaskService.createTask(taskCreateInputDto, principal), HttpStatus.CREATED);
    }

    @Operation(summary = "[Author] Update an existing task",
            description = "This API is used by authors to update an existing task.")
    @PutMapping("/update")
    public ResponseEntity<TaskModel> updateTask(@Valid @RequestBody TaskUpdateInputDto taskUpdateInputDto, Principal principal) {
        return new ResponseEntity<>(authorTaskService.updateTask(taskUpdateInputDto, principal), HttpStatus.CREATED);
    }

    @Operation(summary = "[Author] Get a task by ID",
            description = "This API is used by authors to get a task by its ID.")
    @GetMapping("/task-by-id/{taskId}")
    public ResponseEntity<TaskModel> getTaskById(@PathVariable Long taskId) {
        return new ResponseEntity<>(authorTaskService.findById(taskId), HttpStatus.OK);
    }

    @Operation(summary = "[Author] Get a author tasks by principal",
            description = "This API is used by authors to get all tasks by its principal.")
    @GetMapping("/all-tasks")
    public ResponseEntity<List<TaskModel>> getAllAuthorTasks(Principal principal) {
        return new ResponseEntity<>(authorTaskService.getAllTasksByAuthor(principal), HttpStatus.OK);
    }


    @Operation(summary = "[Author] Delete own task",
            description = "This API is used by authors to delete their own task.")
    @DeleteMapping("/delete-own-task/{id}")
    public ResponseEntity<String> deleteTask(@ApiParam(value = "ID of the task", required = true)
                                                 @PathVariable Long id,
                                                    Principal principal) {
        String msg = String.format("task by id : %s and by user email: %s successfully deleted", id, principal.getName());
        authorTaskService.delete(id, principal);
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

}
