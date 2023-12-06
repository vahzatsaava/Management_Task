package com.example.management_task.mapping;

import com.example.management_task.model.TaskModel;
import com.example.management_task.repository.entity.TaskEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = CommentsMapper.class)
public interface TaskMapper {

    @Mapping(target = "author", source = "taskEntity.author.id")
    @Mapping(target = "executor", source = "taskEntity.executor.id")
    @Mapping(target = "comments", source = "taskEntity.comments")
    TaskModel toTaskModel(TaskEntity taskEntity);
}
