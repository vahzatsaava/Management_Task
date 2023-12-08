package com.example.management_task.controller;

import com.example.management_task.dto.JwtResponse;
import com.example.management_task.dto.SignUpDto;
import com.example.management_task.dto.UserCreateDto;
import com.example.management_task.model.UserModel;
import com.example.management_task.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authServiceImpl;

    @PostMapping("/create")
    public ResponseEntity<UserModel> create(@Valid @RequestBody UserCreateDto userCreateDto) {
        return new ResponseEntity<>(authServiceImpl.createProfile(userCreateDto), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody SignUpDto signUpDto) {
        return new ResponseEntity<>(authServiceImpl.login(signUpDto), HttpStatus.OK);
    }
}
