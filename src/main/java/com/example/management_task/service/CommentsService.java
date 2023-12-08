package com.example.management_task.service;

import com.example.management_task.dto.comments_dto.CommentsInputDto;
import com.example.management_task.dto.comments_dto.CommentsUpdateDto;
import com.example.management_task.model.CommentsModel;
import com.example.management_task.model.TaskModel;

import java.security.Principal;
import java.util.List;

public interface CommentsService {

    TaskModel save(CommentsInputDto commentsInput, Principal principal);

    TaskModel updateComment(CommentsUpdateDto commentsDto, Principal principal);

    void deleteByCommentAuthor(Long id, Principal principal);

    List<CommentsModel> getTaskComments(Long taskId);

    List<CommentsModel> getMyComments(Principal principal);
}
