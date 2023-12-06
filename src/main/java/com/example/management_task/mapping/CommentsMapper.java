package com.example.management_task.mapping;

import com.example.management_task.model.CommentsModel;
import com.example.management_task.repository.entity.Comments;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentsMapper {
    @Mapping(target = "author", source = "author.id")
    CommentsModel toCommentsModel(Comments comments);
}
