package com.example.management_task.service.impl;

import com.example.management_task.dto.comments_dto.CommentsInputDto;
import com.example.management_task.dto.comments_dto.CommentsUpdateDto;
import com.example.management_task.exceprtions.CommentsExistException;
import com.example.management_task.mapping.CommentsMapper;
import com.example.management_task.mapping.TaskMapper;
import com.example.management_task.model.CommentsModel;
import com.example.management_task.model.TaskModel;
import com.example.management_task.repository.CommentsRepository;
import com.example.management_task.repository.entity.Comments;
import com.example.management_task.repository.entity.TaskEntity;
import com.example.management_task.repository.entity.User;
import com.example.management_task.service.CommentsService;
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
public class CommentsServiceImpl implements CommentsService {
    private final CommentsRepository commentsRepository;
    private final CommentsMapper commentsMapper;
    private final TaskMapper taskMapper;
    private final AuthorTaskService authorTaskService;
    private final UserService userService;


    @Override
    @Transactional
    public TaskModel save(CommentsInputDto commentsInput, Principal principal) {
        TaskEntity taskEntity = authorTaskService.findTaskByID(commentsInput.getTaskId());
        Comments comments = new Comments();
        comments.setTask(taskEntity);
        comments.setText(commentsInput.getText());
        comments.setCreated(LocalDateTime.now());

        User commentsAuthor = userService.getCurrentUser(principal);
        comments.setAuthor(commentsAuthor);
        commentsRepository.save(comments);

        return taskMapper.toTaskModel(taskEntity);
    }

    @Override
    @Transactional
    public TaskModel updateComment(CommentsUpdateDto commentsDto, Principal principal) {
        Comments comments = findByIdAndEmail(commentsDto.getCommentsId(), principal.getName());
        comments.setText(commentsDto.getText());
        return taskMapper.toTaskModel(comments.getTask());

    }

    @Override
    @Transactional
    public void deleteByCommentAuthor(Long id, Principal principal) {
        Comments comments = findByIdAndEmail(id, principal.getName());
        commentsRepository.delete(comments);
    }


    @Override
    public List<CommentsModel> getTaskComments(Long taskId) {
        TaskEntity taskEntity = authorTaskService.findTaskByID(taskId);
        return taskEntity.getComments().stream()
                .map(commentsMapper::toCommentsModel)
                .toList();
    }

    @Override
    public List<CommentsModel> getMyComments(Principal principal) {
        User currentUser = userService.getCurrentUser(principal);
        return currentUser.getComments().stream()
                .map(commentsMapper::toCommentsModel)
                .toList();
    }

    public Comments findByIdAndEmail(Long id, String email) {
        return commentsRepository.findByIdAndAuthorEmail(id, email)
                .orElseThrow(() -> new CommentsExistException
                        (String.format("Comment with id %d and email  %s does not exist", id, email)));
    }
}
