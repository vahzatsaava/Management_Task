package com.example.management_task.controller;

import com.example.management_task.dto.tasks_dto.ExecutorTaskInputDto;
import com.example.management_task.model.TaskModel;
import com.example.management_task.service.ExecutorTaskService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/v1/executors/tasks")
public class ExecutorTaskController {
    private final ExecutorTaskService executorTaskService;

    @PostMapping("/change-status")
    public ResponseEntity<TaskModel> changeTaskStatusByExecutor(@Valid @RequestBody ExecutorTaskInputDto inputDto,
                                                                Principal principal) {
        return new ResponseEntity<>(executorTaskService.changeTaskStatus(inputDto, principal), HttpStatus.OK);
    }
}
