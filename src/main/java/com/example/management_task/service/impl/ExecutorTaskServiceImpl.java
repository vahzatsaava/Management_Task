package com.example.management_task.service.impl;

import com.example.management_task.dto.tasks_dto.ExecutorTaskInputDto;
import com.example.management_task.exceprtions.TaskException;
import com.example.management_task.mapping.TaskMapper;
import com.example.management_task.model.TaskModel;
import com.example.management_task.repository.TaskRepository;
import com.example.management_task.repository.entity.TaskEntity;
import com.example.management_task.repository.entity.TuskStatus;
import com.example.management_task.service.ExecutorTaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExecutorTaskServiceImpl implements ExecutorTaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Override
    @Transactional
    public TaskModel changeTaskStatus(ExecutorTaskInputDto inputDto, Principal principal) {
        TaskEntity currentEntity = taskRepository.findByIdAndExecutorEmail(inputDto.getTaskId(), principal.getName())
                .orElseThrow(() -> new TaskException(inputDto.getTaskId(), principal.getName()));

        currentEntity.setStatus(inputDto.getTuskStatus());
        if (inputDto.getTuskStatus() == TuskStatus.DONE) {
            currentEntity.setFinished(LocalDateTime.now());
        }

        return taskMapper.toTaskModel(currentEntity);
    }

}