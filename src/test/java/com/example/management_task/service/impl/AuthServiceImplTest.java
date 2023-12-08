package com.example.management_task.service.impl;

import com.example.management_task.dto.JwtResponse;
import com.example.management_task.dto.SignUpDto;
import com.example.management_task.dto.UserCreateDto;
import com.example.management_task.mapping.UserMapper;
import com.example.management_task.model.UserModel;
import com.example.management_task.redis_session.TokenCasheService;
import com.example.management_task.repository.UserRepository;
import com.example.management_task.repository.entity.Role;
import com.example.management_task.repository.entity.User;
import com.example.management_task.repository.entity.UserStatus;
import com.example.management_task.security.JwtTokenProvider;
import com.example.management_task.security.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsServiceImpl userDetails;

    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private RoleService roleService;

    @Mock
    private TokenCasheService casheService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private AuthServiceImpl authServiceImpl;

    @Test
    void login_WhenUserExistsAndNotDeleted_ShouldReturnJwtResponse() {
        SignUpDto signUpDto = new SignUpDto("test@example.com", "password");
        User existingUser = new User();
        existingUser.setStatus(UserStatus.ACTIVE);

        when(userRepository.findUserByEmail(signUpDto.getEmail())).thenReturn(Optional.of(existingUser));
        when(casheService.getCachedTokenForEmail(signUpDto.getEmail())).thenReturn(Optional.empty());

        UserDetails userDetails = mock(UserDetails.class);
        when(this.userDetails.loadUserByUsername(signUpDto.getEmail())).thenReturn(userDetails);

        String generatedToken = "generatedToken";
        when(tokenProvider.generateToken(userDetails)).thenReturn(generatedToken);

        JwtResponse jwtResponse = authServiceImpl.login(signUpDto);

        assertNotNull(jwtResponse);
        assertEquals(generatedToken, jwtResponse.getToken());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(casheService).cacheToken(generatedToken, signUpDto.getEmail());
    }


    @Test
    void testCreateProfile() {
        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setEmail("test@example.com");
        userCreateDto.setName("Test User");
        userCreateDto.setPassword("password");

        Role role = new Role();
        role.setName("ROLE_USER");

        User createdUser = new User();
        createdUser.setEmail(userCreateDto.getEmail());
        createdUser.setName(userCreateDto.getName());
        createdUser.setPassword("encodedPassword");

        UserModel model = new UserModel();
        model.setEmail("test@example.com");

        when(roleService.findRoleByName("ROLE_USER")).thenReturn(role);
        when(passwordEncoder.encode(userCreateDto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(createdUser);
        when(userMapper.toUserModel(createdUser)).thenReturn(model);

        UserModel userModel = authServiceImpl.createProfile(userCreateDto);

        verify(userRepository, times(1)).save(any(User.class));
        verify(userMapper, times(1)).toUserModel(any(User.class));
        verify(passwordEncoder, times(1)).encode(any(CharSequence.class));
        verify(roleService, times(1)).findRoleByName(any(String.class));
        assertEquals(userModel.getEmail(), "test@example.com");
    }
}
