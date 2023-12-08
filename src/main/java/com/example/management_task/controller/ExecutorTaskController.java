package com.example.management_task.controller;

import com.example.management_task.dto.SearchFilterRequest;
import com.example.management_task.dto.tasks_dto.ExecutorTaskInputDto;
import com.example.management_task.model.TaskModel;
import com.example.management_task.service.ExecutorTaskService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/v1/executors/tasks")
public class ExecutorTaskController {
    private final ExecutorTaskService executorTaskService;

    @PostMapping("/change-status")
    public ResponseEntity<TaskModel> changeTaskStatusByExecutor(@Valid @RequestBody ExecutorTaskInputDto inputDto, Principal principal) {
        return new ResponseEntity<>(executorTaskService.changeTaskStatus(inputDto, principal), HttpStatus.OK);
    }

    @PostMapping("/get-by-filter")
    public ResponseEntity<Page<TaskModel>> getByFilter(@RequestBody SearchFilterRequest request) {
        return new ResponseEntity<>(executorTaskService.getByFilter(request), HttpStatus.OK);
    }
}
