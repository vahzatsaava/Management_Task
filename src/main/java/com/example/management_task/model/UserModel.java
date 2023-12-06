package com.example.management_task.model;

import com.example.management_task.repository.entity.Role;
import lombok.Data;

import java.util.Set;

@Data
public class UserModel {
    private Long id;

    private String name;

    private String email;

    private String password;

    private Set<Role> roles;

}

