package com.example.management_task.service;

import com.example.management_task.dto.tasks_dto.TaskCreateInputDto;
import com.example.management_task.dto.tasks_dto.TaskUpdateInputDto;
import com.example.management_task.model.TaskModel;
import com.example.management_task.repository.entity.TaskEntity;

import java.security.Principal;

public interface AuthorTaskService {
    TaskModel createTask(TaskCreateInputDto taskCreateInputDto, Principal principal);

    TaskModel updateTask(TaskUpdateInputDto updateInputDto,Principal principal);

    TaskModel findById(Long taskId);

    void delete(Long id, Principal principal);

    TaskEntity findTaskByID(Long id);
}
