package com.example.management_task.dto.tasks_dto;

import com.example.management_task.repository.entity.Priority;
import com.example.management_task.repository.entity.TuskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskUpdateInputDto {

    @NotNull(message = "taskId must not be null")
    private Long taskId;

    @NotBlank(message = "header must not be blank")
    @NotNull(message = "header must not be null")
    private String header;

    private String definition;

    private Priority priority;

    private TuskStatus tuskStatus;

    private Long executor;
}
