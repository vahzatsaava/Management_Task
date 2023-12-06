package com.example.management_task.model;

import com.example.management_task.repository.entity.Comments;
import com.example.management_task.repository.entity.Priority;
import com.example.management_task.repository.entity.TuskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskModel {
    private Long id;

    private String header;

    private String definition;

    private TuskStatus status;

    private Priority priority;

    private LocalDateTime created;

    private LocalDateTime finished;

    private Long author;

    private Long executor;

    private List<CommentsModel> comments;
}
