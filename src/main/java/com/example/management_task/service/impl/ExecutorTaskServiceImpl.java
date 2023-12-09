package com.example.management_task.service.impl;

import com.example.management_task.dto.SearchFilterRequest;
import com.example.management_task.dto.tasks_dto.ExecutorTaskInputDto;
import com.example.management_task.exceprtions.TaskException;
import com.example.management_task.mapping.TaskMapper;
import com.example.management_task.model.TaskModel;
import com.example.management_task.repository.TaskRepository;
import com.example.management_task.repository.entity.TaskEntity;
import com.example.management_task.repository.entity.TuskStatus;
import com.example.management_task.repository.entity.User;
import com.example.management_task.repository.pagination.TuskSpecifications;
import com.example.management_task.service.ExecutorTaskService;
import com.example.management_task.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExecutorTaskServiceImpl implements ExecutorTaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final UserService userService;

    @Override
    @Transactional
    public TaskModel changeTaskStatus(ExecutorTaskInputDto inputDto, Principal principal) {
        TaskEntity currentEntity = taskRepository.findByIdAndExecutorEmail(inputDto.getTaskId(), principal.getName())
                .orElseThrow(
                        () -> new TaskException(String.format("user: %s is not a executor of this task %s",principal.getName(),inputDto.getTaskId())));

        currentEntity.setStatus(inputDto.getTuskStatus());
        if (inputDto.getTuskStatus() == TuskStatus.DONE) {
            currentEntity.setFinished(LocalDateTime.now());
        }

        return taskMapper.toTaskModel(currentEntity);
    }
    @Override
    public List<TaskModel> getExecutorsTasks(Principal principal){
        User current = userService.getCurrentUser(principal);

        return current.getExecutedTasks().stream()
                .map(taskMapper::toTaskModel)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaskModel> getByFilter(SearchFilterRequest filterRequest) {
        var specification = new TuskSpecifications(filterRequest);
        var page = PageRequest.of(filterRequest.getPageNumber(), filterRequest.getPageSize());

        return taskRepository.findAll(specification, page)
                .map(taskMapper::toTaskModel);
    }

}
