package com.example.management_task.mapping;

import com.example.management_task.model.UserModel;
import com.example.management_task.repository.entity.User;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface UserMapper {
    UserModel toUserModel(User user);
}
