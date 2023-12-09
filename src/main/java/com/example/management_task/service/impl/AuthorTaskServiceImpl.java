package com.example.management_task.service.impl;

import com.example.management_task.dto.tasks_dto.TaskCreateInputDto;
import com.example.management_task.dto.tasks_dto.TaskUpdateInputDto;
import com.example.management_task.exceprtions.TaskException;
import com.example.management_task.mapping.TaskMapper;
import com.example.management_task.model.TaskModel;
import com.example.management_task.repository.TaskRepository;
import com.example.management_task.repository.entity.TaskEntity;
import com.example.management_task.repository.entity.TuskStatus;
import com.example.management_task.repository.entity.User;
import com.example.management_task.service.AuthorTaskService;
import com.example.management_task.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorTaskServiceImpl implements AuthorTaskService {
    private final TaskRepository taskRepository;
    private final UserService userService;
    private final TaskMapper taskMapper;

    @Override
    @Transactional
    public TaskModel createTask(TaskCreateInputDto taskCreateInputDto, Principal principal) {
        TaskEntity taskEntity = new TaskEntity();
        User author = userService.getCurrentUser(principal);

        taskEntity.setDefinition(taskCreateInputDto.getDefinition());
        taskEntity.setHeader(taskCreateInputDto.getHeader());
        taskEntity.setPriority(taskCreateInputDto.getPriority());


        User executor = userService.findUserById(taskCreateInputDto.getExecutor());
        taskEntity.setExecutor(executor);

        taskEntity.setAuthor(author);
        taskEntity.setCreated(LocalDateTime.now());
        taskEntity.setStatus(TuskStatus.IN_PROGRESS);
        taskEntity.setFinished(null);

        TaskEntity savedTusk = taskRepository.save(taskEntity);

        return taskMapper.toTaskModel(savedTusk);
    }

    @Override
    @Transactional
    public TaskModel updateTask(TaskUpdateInputDto updateInputDto, Principal principal) {

        TaskEntity currentEntity = findByIdAndAuthorEmail(updateInputDto.getTaskId(), principal.getName());
        currentEntity.setDefinition(updateInputDto.getDefinition());
        currentEntity.setHeader(updateInputDto.getHeader());
        currentEntity.setPriority(updateInputDto.getPriority());

        if (updateInputDto.getTuskStatus() == TuskStatus.DONE) {
            currentEntity.setFinished(LocalDateTime.now());
        }

        User executor = userService.findUserById(updateInputDto.getExecutor());
        currentEntity.setExecutor(executor);


        return taskMapper.toTaskModel(currentEntity);
    }

    @Override
    public TaskModel findById(Long taskId) {
        return taskMapper.toTaskModel(findTaskByID(taskId));
    }

    @Override
    @Transactional
    public void delete(Long id, Principal principal) {
        TaskEntity currentTask = findByIdAndAuthorEmail(id, principal.getName());
        taskRepository.delete(currentTask);
    }

    @Override
    public TaskEntity findTaskByID(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskException("Task is not exist by id " + id));
    }

    @Override
    public List<TaskModel> getAllTasksByAuthor(Principal principal) {
        User currentUser = userService.getCurrentUser(principal);
        return currentUser.getAuthoredTasks()
                .stream()
                .map(taskMapper::toTaskModel)
                .toList();
    }

    private TaskEntity findByIdAndAuthorEmail(Long id, String email) {
        return taskRepository.findByIdAndAuthorEmail(id, email)
                .orElseThrow(() -> new TaskException(id, email));
    }
}
