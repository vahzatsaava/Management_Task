package com.example.management_task.dto;

import com.example.management_task.repository.entity.Priority;
import com.example.management_task.repository.entity.TuskStatus;
import lombok.Data;


@Data
public class SearchFilterRequest {
    private Long authorId;
    private Long executorId;
    private String search;
    private TuskStatus tuskStatus;
    private Priority priority;

    private Integer pageSize = 10;
    private Integer pageNumber = 0;
}
