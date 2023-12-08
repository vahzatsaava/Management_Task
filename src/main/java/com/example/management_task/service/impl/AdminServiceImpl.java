package com.example.management_task.service.impl;

import com.example.management_task.mapping.UserMapper;
import com.example.management_task.model.UserModel;
import com.example.management_task.redis_session.TokenCasheService;
import com.example.management_task.repository.UserRepository;
import com.example.management_task.repository.entity.User;
import com.example.management_task.repository.entity.UserStatus;
import com.example.management_task.security.TokenBlacklistService;
import com.example.management_task.service.AdminService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final TokenCasheService casheService;
    private final TokenBlacklistService tokenBlacklistService;

    private final UserMapper userMapper;

    @Override
    @Transactional
    public void deleteProfile(String email) {
        User currentUser = findByEmail(email);
        currentUser.setStatus(UserStatus.DELETE);

        Optional<String> jwt = casheService.getCachedTokenForEmail(email);
        jwt.ifPresent(tokenBlacklistService::blacklistToken);
        casheService.delete(email);
    }

    @Override
    @Transactional
    public UserModel addAdminRightsToCurrentUser(String email) {
        User userByPrincipal = findByEmail(email);
        userByPrincipal.setRoles(Set.of(roleService.findRoleByName("ROLE_USER"), roleService.findRoleByName("ROLE_ADMIN")));

        return userMapper.toUserModel(userByPrincipal);
    }

    private User findByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("user not found by email: " + email));
    }
}
