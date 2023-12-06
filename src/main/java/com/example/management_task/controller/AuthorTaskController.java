package com.example.management_task.controller;

import com.example.management_task.dto.tasks_dto.TaskCreateInputDto;
import com.example.management_task.dto.tasks_dto.TaskUpdateInputDto;
import com.example.management_task.model.TaskModel;
import com.example.management_task.service.AuthorTaskService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/v1/authors/tasks")
public class AuthorTaskController {
    private final AuthorTaskService authorTaskService;

    @PostMapping("/create")
    public ResponseEntity<TaskModel> createTask(@Valid @RequestBody TaskCreateInputDto taskCreateInputDto, Principal principal) {
        return new ResponseEntity<>(authorTaskService.createTask(taskCreateInputDto, principal), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<TaskModel> createTask(@Valid @RequestBody TaskUpdateInputDto taskUpdateInputDto, Principal principal) {
        return new ResponseEntity<>(authorTaskService.updateTask(taskUpdateInputDto, principal), HttpStatus.CREATED);
    }

    @GetMapping("/task-by-id/{taskId}")
    public ResponseEntity<TaskModel> getTaskById(@PathVariable Long taskId) {
        return new ResponseEntity<>(authorTaskService.findById(taskId), HttpStatus.OK);
    }

    @DeleteMapping("/delete-own-task/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id, Principal principal) {
        String msg = String.format("task by id : %s and by user email: %s successfully deleted", id, principal.getName());
        authorTaskService.delete(id, principal);
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

}
