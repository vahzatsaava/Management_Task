package com.example.management_task.service.impl;

import com.example.management_task.dto.SearchFilterRequest;
import com.example.management_task.dto.tasks_dto.ExecutorTaskInputDto;
import com.example.management_task.mapping.TaskMapper;
import com.example.management_task.model.TaskModel;
import com.example.management_task.repository.TaskRepository;
import com.example.management_task.repository.entity.TaskEntity;
import com.example.management_task.repository.entity.TuskStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExecutorTaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private ExecutorTaskServiceImpl executorTaskService;

    @Test
    void changeTaskStatus() {
        ExecutorTaskInputDto executorTaskInputDto = new ExecutorTaskInputDto();
        executorTaskInputDto.setTaskId(1L);
        executorTaskInputDto.setTuskStatus(TuskStatus.DONE);

        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("author@example.com");

        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setStatus(TuskStatus.DONE);
        taskEntity.setId(1L);

        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(TuskStatus.DONE);
        taskModel.setId(1L);

        when(taskRepository.findByIdAndExecutorEmail(1L, "author@example.com")).thenReturn(Optional.of(taskEntity));
        when(taskMapper.toTaskModel(taskEntity)).thenReturn(taskModel);


        TaskModel model = executorTaskService.changeTaskStatus(executorTaskInputDto,principal);

        verify(taskRepository,times(1)).findByIdAndExecutorEmail(anyLong(),anyString());
        verify(taskMapper,times(1)).toTaskModel(any(TaskEntity.class));

        assertEquals(model.getId(),executorTaskInputDto.getTaskId());
        assertEquals(model.getStatus(),executorTaskInputDto.getTuskStatus());
    }

    @Test
    void getByFilter() {
        SearchFilterRequest filterRequest = new SearchFilterRequest();
        filterRequest.setPageNumber(0);
        filterRequest.setPageSize(10);

        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setStatus(TuskStatus.DONE);
        taskEntity.setId(1L);

        List<TaskEntity> taskEntities = Arrays.asList(
                    taskEntity
        );

        Page<TaskEntity> taskEntityPage = new PageImpl<>(taskEntities);

        when(taskRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(taskEntityPage);

        when(taskMapper.toTaskModel(any(TaskEntity.class)))
                .thenReturn(new TaskModel());

        Page<TaskModel> resultPage = executorTaskService.getByFilter(filterRequest);


        assertNotNull(resultPage);
        assertEquals(taskEntities.size(), resultPage.getTotalElements());
        verify(taskRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
        verify(taskMapper, times(taskEntities.size())).toTaskModel(any(TaskEntity.class));
    }
}