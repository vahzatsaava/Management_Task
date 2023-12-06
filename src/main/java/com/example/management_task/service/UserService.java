package com.example.management_task.service;

import com.example.management_task.dto.JwtResponse;
import com.example.management_task.dto.SignUpDto;
import com.example.management_task.dto.UserCreateDto;
import com.example.management_task.model.UserModel;
import com.example.management_task.repository.entity.User;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

public interface UserService {

    UserModel findUserByEmail(String email);

    UserModel updateProfile(UserCreateDto userCreateDto, Principal principal);

    void deleteOwnProfile(Principal principal);

    User getCurrentUser(Principal principal);

    User findUserById(Long userId);
}
