package com.example.management_task.dto.comments_dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentsInputDto {
    @NotNull(message = "taskId must not be null")
    private Long taskId;

    @NotNull(message = "userId must not be null")
    private Long userId;

    @NotNull(message = "text must not be null")
    @NotBlank(message = "text must not be blank")
    private String text;
}
