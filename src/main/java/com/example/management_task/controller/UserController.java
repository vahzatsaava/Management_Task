package com.example.management_task.controller;

import com.example.management_task.dto.UserCreateDto;
import com.example.management_task.model.UserModel;
import com.example.management_task.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @Operation(summary = "Update user profile",
            description = "This API is used to update the profile of a user. If you will update your password or email you should to login again")
    @PutMapping("/update")
    public ResponseEntity<UserModel> update(@Valid @RequestBody UserCreateDto userCreateDto, Principal principal) {
        return new ResponseEntity<>(userService.updateProfile(userCreateDto,principal), HttpStatus.OK);
    }

    @Operation(summary = "Delete own account, and logout",
            description = "This API is used to delete the account of the authenticated user. ")
    @DeleteMapping("/delete-own-account")
    public ResponseEntity<String> deleteOwnAccount(Principal principal) {
        userService.deleteOwnProfile(principal);
        String deletedMsg = String.format("user with email: %s was deleted", principal.getName());
        return new ResponseEntity<>(deletedMsg, HttpStatus.CREATED);
    }

    @Operation(summary = "Get user by email",
            description = "This API is used to retrieve user information based on the provided email.")
    @GetMapping("/get-by-email")
    public ResponseEntity<UserModel> getUserByEmail(String email){
        return new ResponseEntity<>(userService.findUserByEmail(email),HttpStatus.OK);
    }

}
