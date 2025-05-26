package com.byteshifttech.userapi.service;

import com.byteshifttech.userapi.dto.AuthResponse;
import com.byteshifttech.userapi.dto.LoginRequest;
import com.byteshifttech.userapi.dto.RegisterRequest;
import com.byteshifttech.userapi.entity.User;
import com.byteshifttech.userapi.exception.CustomAppException;
import com.byteshifttech.userapi.repository.UserRepository;
import com.byteshifttech.userapi.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldRegisterNewUser() {
        RegisterRequest request = new RegisterRequest("John", "john@example.com", "pass123");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPass");
        when(jwtUtil.generateToken(request.getEmail())).thenReturn("dummyToken");

        AuthResponse response = authService.register(request);

        assertEquals("dummyToken", response.getToken());
        assertEquals("User registered successfully", response.getMessage());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowIfEmailExists() {
        RegisterRequest request = new RegisterRequest("Jane", "jane@example.com", "pass456");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        CustomAppException ex = assertThrows(CustomAppException.class, () -> authService.register(request));
        assertEquals("Email is already registered", ex.getMessage());
        assertEquals(409, ex.getStatusCode());
    }

    @Test
    void shouldAuthenticateSuccessfully() {
        LoginRequest request = new LoginRequest("john@example.com", "pass123");
        User user = new User(1L, "John", request.getEmail(), "encodedPass", null);

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtUtil.generateToken(request.getEmail())).thenReturn("jwtToken");

        AuthResponse response = authService.authenticate(request);

        assertEquals("jwtToken", response.getToken());
        assertEquals("Login successful", response.getMessage());
    }

    @Test
    void shouldFailAuthenticationOnWrongPassword() {
        LoginRequest request = new LoginRequest("john@example.com", "wrongPass");
        User user = new User(1L, "John", request.getEmail(), "encodedPass", null);

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(false);

        CustomAppException ex = assertThrows(CustomAppException.class, () -> authService.authenticate(request));
        assertEquals("Invalid credentials", ex.getMessage());
        assertEquals(400, ex.getStatusCode());
    }

    @Test
    void shouldFailAuthenticationIfUserNotFound() {
        LoginRequest request = new LoginRequest("unknown@example.com", "pass123");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        CustomAppException ex = assertThrows(CustomAppException.class, () -> authService.authenticate(request));
        assertEquals("Invalid credentials", ex.getMessage());
        assertEquals(400, ex.getStatusCode());
    }
}
