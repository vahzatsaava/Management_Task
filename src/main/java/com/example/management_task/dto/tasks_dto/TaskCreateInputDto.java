package com.example.management_task.dto.tasks_dto;

import com.example.management_task.repository.entity.Priority;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskCreateInputDto {

    @NotBlank(message = "header must not be blank")
    @NotNull(message = "header must not be null")
    private String header;

    @NotBlank(message = "definition must not be blank")
    @NotNull(message = "definition must not be null")
    private String definition;

    private Priority priority;

    private Long executor;
}
