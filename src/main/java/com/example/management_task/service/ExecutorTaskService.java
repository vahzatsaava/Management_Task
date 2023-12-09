package com.example.management_task.service;

import com.example.management_task.dto.SearchFilterRequest;
import com.example.management_task.dto.tasks_dto.ExecutorTaskInputDto;
import com.example.management_task.model.TaskModel;
import org.springframework.data.domain.Page;

import java.security.Principal;
import java.util.List;

public interface ExecutorTaskService {
    TaskModel changeTaskStatus(ExecutorTaskInputDto inputDto, Principal principal);

    List<TaskModel> getExecutorsTasks(Principal principal);

    Page<TaskModel> getByFilter(SearchFilterRequest filterRequest);
}
