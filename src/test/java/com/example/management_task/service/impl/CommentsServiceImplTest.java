package com.example.management_task.service.impl;

import com.example.management_task.dto.comments_dto.CommentsInputDto;
import com.example.management_task.dto.comments_dto.CommentsUpdateDto;
import com.example.management_task.mapping.CommentsMapper;
import com.example.management_task.mapping.TaskMapper;
import com.example.management_task.model.CommentsModel;
import com.example.management_task.model.TaskModel;
import com.example.management_task.repository.CommentsRepository;
import com.example.management_task.repository.entity.Comments;
import com.example.management_task.repository.entity.TaskEntity;
import com.example.management_task.repository.entity.User;
import com.example.management_task.service.AuthorTaskService;
import com.example.management_task.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CommentsServiceImplTest {

    @InjectMocks
    private CommentsServiceImpl commentsService;

    @Mock
    private CommentsRepository commentsRepository;

    @Mock
    private CommentsMapper commentsMapper;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private AuthorTaskService authorTaskService;

    @Mock
    private UserService userService;

    @Test
    void save_ShouldSaveCommentAndReturnTaskModel() {
        Principal principal = mock(Principal.class);

        CommentsInputDto commentsInput = new CommentsInputDto();
        commentsInput.setTaskId(1L);
        commentsInput.setText("Test comment");


        TaskEntity taskEntity = new TaskEntity();
        User commentsAuthor = new User();
        Comments savedComment = new Comments();
        savedComment.setTask(taskEntity);

        TaskModel model = new TaskModel();
        model.setHeader("header");

        when(authorTaskService.findTaskByID(1L)).thenReturn(taskEntity);
        when(userService.getCurrentUser(any(Principal.class))).thenReturn(commentsAuthor);
        when(commentsRepository.save(any(Comments.class))).thenReturn(savedComment);
        when(taskMapper.toTaskModel(taskEntity)).thenReturn(model);

        TaskModel result = commentsService.save(commentsInput,principal);

        verify(commentsRepository).save(any(Comments.class));
        verify(commentsRepository, times(1)).save(any(Comments.class));

        assertEquals(result.getHeader(), model.getHeader());
    }

    @Test
    void updateComment_ShouldUpdateCommentAndReturnTaskModel() {
        CommentsUpdateDto commentsDto = new CommentsUpdateDto();
        commentsDto.setCommentsId(1L);
        commentsDto.setText("Updated comment");
        Principal principal = mock(Principal.class);

        Comments existingComment = new Comments();
        existingComment.setTask(new TaskEntity());

        TaskModel model = new TaskModel();
        model.setHeader("header");

        when(taskMapper.toTaskModel(any(TaskEntity.class))).thenReturn(model);
        when(commentsRepository.findByIdAndAuthorEmail(1L, principal.getName())).thenReturn(Optional.of(existingComment));
        TaskModel result = commentsService.updateComment(commentsDto, principal);


        verify(taskMapper, times(1)).toTaskModel(any(TaskEntity.class));
        assertEquals(result.getHeader(), model.getHeader());
    }

    @Test
    void deleteByCommentAuthor_ShouldDeleteComment() {
        Principal principal = mock(Principal.class);
        Comments existingComment = new Comments();

        when(commentsRepository.findByIdAndAuthorEmail(1L, principal.getName())).thenReturn(Optional.of(existingComment));

        commentsService.deleteByCommentAuthor(1L, principal);

        verify(commentsRepository).delete(existingComment);
    }

}