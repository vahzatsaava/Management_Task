package com.example.management_task.service.impl;

import com.example.management_task.dto.JwtResponse;
import com.example.management_task.dto.SignUpDto;
import com.example.management_task.dto.UserCreateDto;
import com.example.management_task.mapping.UserMapper;
import com.example.management_task.model.UserModel;
import com.example.management_task.redis_session.TokenCasheService;
import com.example.management_task.repository.UserRepository;
import com.example.management_task.repository.entity.User;
import com.example.management_task.repository.entity.UserStatus;
import com.example.management_task.security.JwtTokenProvider;
import com.example.management_task.security.UserDetailsServiceImpl;
import com.example.management_task.service.AuthService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager manager;
    private final UserDetailsServiceImpl userDetails;
    private final JwtTokenProvider tokenProvider;
    private final RoleService roleService;
    private final TokenCasheService casheService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    @Override
    @Transactional
    public JwtResponse login(SignUpDto signUpDto) {
        User current = userRepository.findUserByEmail(signUpDto.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("user not found by email: " + signUpDto.getEmail()));

        if (current.getStatus() == UserStatus.DELETE) {
            throw new AccessDeniedException("User " + signUpDto.getEmail() + " was deleted before");
        }

        Optional<String> cachedToken = casheService.getCachedTokenForEmail(signUpDto.getEmail());

        if (cachedToken.isPresent()) {
            return new JwtResponse(cachedToken.get());
        } else {
            manager.authenticate(new UsernamePasswordAuthenticationToken(signUpDto.getEmail(), signUpDto.getPassword()));
            UserDetails loadUserByUsername = userDetails.loadUserByUsername(signUpDto.getEmail());
            String token = tokenProvider.generateToken(loadUserByUsername);
            casheService.cacheToken(token, signUpDto.getEmail());
            return new JwtResponse(token);
        }
    }
    @Override
    @Transactional
    public UserModel createProfile(UserCreateDto userCreateDto) {
        User created = new User();
        created.setEmail(userCreateDto.getEmail());
        created.setName(userCreateDto.getName());
        created.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
        created.setRoles(Set.of(roleService.findRoleByName("ROLE_USER")));
        created.setStatus(UserStatus.ACTIVE);

        User userSaved = userRepository.save(created);
        return userMapper.toUserModel(userSaved);
    }
}
