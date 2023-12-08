package com.example.management_task.service.impl;

import com.example.management_task.dto.UserCreateDto;
import com.example.management_task.mapping.UserMapper;
import com.example.management_task.model.UserModel;
import com.example.management_task.redis_session.TokenCasheService;
import com.example.management_task.repository.UserRepository;
import com.example.management_task.repository.entity.User;
import com.example.management_task.repository.entity.UserStatus;
import com.example.management_task.security.TokenBlacklistService;
import com.example.management_task.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final TokenCasheService casheService;
    private final TokenBlacklistService tokenBlacklistService;


    @Override
    public UserModel findUserByEmail(String email) {
        User user = findByEmail(email);
        return userMapper.toUserModel(user);
    }

    @Override
    @Transactional
    public UserModel updateProfile(UserCreateDto userCreateDto, Principal principal) {
        User created = findByEmail(principal.getName());
        created.setEmail(userCreateDto.getEmail());
        created.setName(userCreateDto.getName());
        created.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));

        Optional<String> jwt = casheService.getCachedTokenForEmail(principal.getName());
        jwt.ifPresent(tokenBlacklistService::blacklistToken);
        casheService.delete(principal.getName());

        return userMapper.toUserModel(created);
    }

    @Override
    @Transactional
    public void deleteOwnProfile(Principal principal) {
        User userByPrincipal = findByEmail(principal.getName());
        userByPrincipal.setStatus(UserStatus.DELETE);


        Optional<String> jwt = casheService.getCachedTokenForEmail(principal.getName());
        jwt.ifPresent(tokenBlacklistService::blacklistToken);
        casheService.delete(principal.getName());

    }

    @Override
    public User getCurrentUser(Principal principal) {
        return findByEmail(principal.getName());
    }

    @Override
    public User findUserById(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("user not found by id: " + userId));
    }


    private User findByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("user not found by email: " + email));
    }

}
