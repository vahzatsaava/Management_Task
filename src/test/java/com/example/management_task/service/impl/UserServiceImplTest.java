package com.example.management_task.service.impl;

import com.example.management_task.dto.UserCreateDto;
import com.example.management_task.mapping.UserMapper;
import com.example.management_task.model.UserModel;
import com.example.management_task.redis_session.TokenCasheService;
import com.example.management_task.repository.UserRepository;
import com.example.management_task.repository.entity.User;
import com.example.management_task.repository.entity.UserStatus;
import com.example.management_task.security.TokenBlacklistService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.Principal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @Mock
    private TokenCasheService casheService;

    @Mock
    private TokenBlacklistService tokenBlacklistService;

    @Test
    void findUserByEmail_ShouldReturnUserModel() {

        String email = "test@example.com";
        User user = new User();
        UserModel userModel = new UserModel();
        userModel.setEmail(email);
        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));
        when(userMapper.toUserModel(user)).thenReturn(userModel);


        UserModel result = userService.findUserByEmail(email);

        assertEquals(userModel.getEmail(), result.getEmail());
    }

    @Test
    void updateProfile_ShouldUpdateUserAndReturnUserModel() {
        UserCreateDto userCreateDto = new UserCreateDto("new-email@example.com", "New Name", "new-password");
        Principal principal = mock(Principal.class);
        User user = new User();

        UserModel model = new UserModel();
        model.setEmail("test@example.com");
        model.setPassword("encoded-password");

        when(principal.getName()).thenReturn("test@example.com");
        when(userRepository.findUserByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("new-password")).thenReturn("encoded-password");
        when(casheService.getCachedTokenForEmail("test@example.com")).thenReturn(Optional.of("cached-token"));
        when(userMapper.toUserModel(user)).thenReturn(model);

        UserModel result = userService.updateProfile(userCreateDto, principal);

        assertEquals(result.getEmail(), model.getEmail());
        assertEquals(result.getPassword(), user.getPassword());
        verify(tokenBlacklistService).blacklistToken("cached-token");
        verify(casheService).delete("test@example.com");
    }

    @Test
    void deleteOwnProfile_ShouldDeleteUserAndInvalidateToken() {
        Principal principal = mock(Principal.class);
        User user = new User();
        when(principal.getName()).thenReturn("test@example.com");
        when(userRepository.findUserByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(casheService.getCachedTokenForEmail("test@example.com")).thenReturn(Optional.of("cached-token"));

        userService.deleteOwnProfile(principal);

        assertEquals(UserStatus.DELETE, user.getStatus());
        verify(tokenBlacklistService).blacklistToken("cached-token");
        verify(casheService).delete("test@example.com");
    }

    @Test
    void getCurrentUser_ShouldReturnCurrentUser() {
        Principal principal = mock(Principal.class);
        User user = new User();
        user.setEmail("test@example.com");
        when(principal.getName()).thenReturn("test@example.com");
        when(userRepository.findUserByEmail("test@example.com")).thenReturn(Optional.of(user));

        User result = userService.getCurrentUser(principal);

        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    void findUserById_ShouldReturnUserById() {
        Long userId = 1L;
        User user = new User();
        user.setEmail("test@example.com");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User result = userService.findUserById(userId);

        assertEquals(user.getEmail(), result.getEmail());
    }
}