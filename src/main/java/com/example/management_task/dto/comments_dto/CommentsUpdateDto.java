package com.example.management_task.dto.comments_dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentsUpdateDto {
    @NotNull(message = "commentsId must not be null")
    private Long commentsId;

    @NotNull(message = "text must not be null")
    @NotBlank(message = "text must not be blank")
    private String text;
}
