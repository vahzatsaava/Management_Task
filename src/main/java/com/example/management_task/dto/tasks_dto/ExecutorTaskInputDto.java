package com.example.management_task.dto.tasks_dto;

import com.example.management_task.repository.entity.TuskStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ExecutorTaskInputDto {

    @NotNull(message = "taskId must not be null")
    private Long taskId;

    @NotNull(message = "tuskStatus must not be null")
    private TuskStatus tuskStatus;
}
