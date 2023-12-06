package com.example.management_task.service;

import com.example.management_task.model.UserModel;

public interface AdminService {

    void deleteProfile(String email);
    UserModel addAdminRightsToCurrentUser(String email);
}
