package com.example.management_task.service;

import com.example.management_task.dto.tasks_dto.ExecutorTaskInputDto;
import com.example.management_task.model.TaskModel;

import java.security.Principal;

public interface ExecutorTaskService {
    TaskModel changeTaskStatus(ExecutorTaskInputDto inputDto, Principal principal);
}
