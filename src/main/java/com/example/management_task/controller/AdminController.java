package com.example.management_task.controller;

import com.example.management_task.model.UserModel;
import com.example.management_task.service.AdminService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@SecurityRequirement(name = "bearerAuth")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admins")
public class AdminController {
    private final AdminService adminService;

    @PatchMapping("/add-admin-role/to-user")
    public ResponseEntity<UserModel> addAdminRightsToUser(String email){
        return new ResponseEntity<>(adminService.addAdminRightsToCurrentUser(email), HttpStatus.OK);
    }

    @DeleteMapping("/delete-user")
    public ResponseEntity<String> deleteUserAccount(String email) {
        adminService.deleteProfile(email);
        String deletedMsg = String.format("user with email: %s was deleted", email);
        return new ResponseEntity<>(deletedMsg, HttpStatus.CREATED);
    }
}
