package com.example.management_task.controller;

import com.example.management_task.dto.SearchFilterRequest;
import com.example.management_task.dto.tasks_dto.ExecutorTaskInputDto;
import com.example.management_task.model.TaskModel;
import com.example.management_task.service.ExecutorTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/v1/executors/tasks")
public class ExecutorTaskController {
    private final ExecutorTaskService executorTaskService;

    @Operation(summary = "Change task status by executor",
            description = "This API is used by executors to change the status of a task.")
    @PostMapping("/change-status")
    public ResponseEntity<TaskModel> changeTaskStatusByExecutor(@Valid @RequestBody ExecutorTaskInputDto inputDto, Principal principal) {
        return new ResponseEntity<>(executorTaskService.changeTaskStatus(inputDto, principal), HttpStatus.OK);
    }

    @Operation(summary = "Get all task by executor",
            description = "This API is used by executors to get all his tasks.")
    @GetMapping("/get-tasks-by-executor")
    public ResponseEntity<List<TaskModel>> getTasksByExecutor(Principal principal) {
        return new ResponseEntity<>(executorTaskService.getExecutorsTasks(principal), HttpStatus.OK);
    }

    @Operation(summary = "Get tasks by filter (author,executor,status,priority,and messages) ",
            description = "This API is used  to get tasks based on the specified filter.")
    @PostMapping("/get-by-filter")
    public ResponseEntity<Page<TaskModel>> getByFilter(@RequestBody SearchFilterRequest request) {
        return new ResponseEntity<>(executorTaskService.getByFilter(request), HttpStatus.OK);
    }
}
