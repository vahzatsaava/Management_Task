package com.example.management_task.service;

import com.example.management_task.dto.JwtResponse;
import com.example.management_task.dto.SignUpDto;
import com.example.management_task.dto.UserCreateDto;
import com.example.management_task.model.UserModel;


public interface AuthService {

    JwtResponse login(SignUpDto signUpDto);

    UserModel createProfile(UserCreateDto userCreateDto);
}
