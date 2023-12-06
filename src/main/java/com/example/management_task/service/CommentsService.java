package com.example.management_task.service;

import com.example.management_task.dto.comments_dto.CommentsInputDto;
import com.example.management_task.dto.comments_dto.CommentsUpdateDto;
import com.example.management_task.model.CommentsModel;
import com.example.management_task.model.TaskModel;

import java.util.List;

public interface CommentsService {
    TaskModel save(CommentsInputDto comments);

    TaskModel updateComment(CommentsUpdateDto commentsDto);

    void delete(Long id);

    List<CommentsModel> getTaskComments(Long taskId);
}
