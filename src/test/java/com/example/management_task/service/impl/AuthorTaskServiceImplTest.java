package com.example.management_task.service.impl;

import com.example.management_task.dto.tasks_dto.TaskCreateInputDto;
import com.example.management_task.dto.tasks_dto.TaskUpdateInputDto;
import com.example.management_task.mapping.TaskMapper;
import com.example.management_task.model.TaskModel;
import com.example.management_task.repository.TaskRepository;
import com.example.management_task.repository.entity.Priority;
import com.example.management_task.repository.entity.TaskEntity;
import com.example.management_task.repository.entity.TuskStatus;
import com.example.management_task.repository.entity.User;
import com.example.management_task.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.Principal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorTaskServiceImplTest {
    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserService userService;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private AuthorTaskServiceImpl authorTaskService;

    @Test
    void createTask() {
        TaskCreateInputDto taskCreateInputDto = new TaskCreateInputDto();
        taskCreateInputDto.setDefinition("Task definition");
        taskCreateInputDto.setHeader("Task header");
        taskCreateInputDto.setPriority(Priority.HIGH);
        taskCreateInputDto.setExecutor(1L);

        Principal principal = mock(Principal.class);
        when(userService.getCurrentUser(principal)).thenReturn(new User());

        TaskEntity savedTaskEntity = new TaskEntity();
        savedTaskEntity.setId(1L);
        when(taskRepository.save(any(TaskEntity.class))).thenReturn(savedTaskEntity);

        TaskModel expectedTaskModel = new TaskModel();
        expectedTaskModel.setId(1L);
        when(taskMapper.toTaskModel(savedTaskEntity)).thenReturn(expectedTaskModel);

        TaskModel result = authorTaskService.createTask(taskCreateInputDto, principal);


        verify(taskRepository,times(1)).save(any(TaskEntity.class));
        verify(taskMapper,times(1)).toTaskModel(any(TaskEntity.class));
        assertEquals(1L, result.getId());
    }

    @Test
    void updateTask() {
        TaskUpdateInputDto updateInputDto = new TaskUpdateInputDto();
        updateInputDto.setTaskId(1L);
        updateInputDto.setDefinition("Updated definition");
        updateInputDto.setHeader("Updated header");
        updateInputDto.setPriority(Priority.LOW);
        updateInputDto.setExecutor(1L);
        updateInputDto.setTuskStatus(TuskStatus.DONE);

        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("author@example.com");

        TaskEntity existingTaskEntity = new TaskEntity();
        existingTaskEntity.setId(1L);

        when(taskRepository.findByIdAndAuthorEmail(1L, "author@example.com")).thenReturn(Optional.of(existingTaskEntity));

        TaskModel expectedTaskModel = new TaskModel();
        expectedTaskModel.setId(1L);
        when(taskMapper.toTaskModel(existingTaskEntity)).thenReturn(expectedTaskModel);

        TaskModel result = authorTaskService.updateTask(updateInputDto, principal);
        verify(taskRepository,times(1)).findByIdAndAuthorEmail(anyLong(),anyString());
        verify(taskMapper,times(1)).toTaskModel(any(TaskEntity.class));

        assertEquals(result.getId(), expectedTaskModel.getId());
    }

    @Test
    void findById() {
        TaskEntity existingTaskEntity = new TaskEntity();
        existingTaskEntity.setId(1L);
        TaskModel expectedTaskModel = new TaskModel();
        expectedTaskModel.setId(1L);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTaskEntity));
        when(taskMapper.toTaskModel(any(TaskEntity.class))).thenReturn(expectedTaskModel);

        TaskModel model = authorTaskService.findById(1L);

        verify(taskRepository,times(1)).findById(anyLong());
        verify(taskMapper,times(1)).toTaskModel(any(TaskEntity.class));

        assertEquals(model.getId(),existingTaskEntity.getId());

    }

    @Test
    void delete() {
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("author@example.com");
        TaskEntity existingTaskEntity = new TaskEntity();
        existingTaskEntity.setId(1L);
        when(taskRepository.findByIdAndAuthorEmail(1L, "author@example.com")).thenReturn(Optional.of(existingTaskEntity));

        authorTaskService.delete(1L,principal);
        // Используйте anyString() вместо eq(principal.getName())
        verify(taskRepository, times(1)).findByIdAndAuthorEmail(anyLong(),anyString());
        verify(taskRepository, times(1)).delete(any(TaskEntity.class));
    }



}