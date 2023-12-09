package com.example.management_task.dto;

import com.example.management_task.repository.entity.Priority;
import com.example.management_task.repository.entity.TuskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;



@Data
public class SearchFilterRequest {
    private Long authorId;
    private Long executorId;
    private String search;
    private TuskStatus tuskStatus;
    private Priority priority;

    @Schema(defaultValue = "10")
    private Integer pageSize;
    private Integer pageNumber;
}
