package com.byteshifttech.userapi.controller;

import com.byteshifttech.userapi.dto.AuthResponse;
import com.byteshifttech.userapi.dto.LoginRequest;
import com.byteshifttech.userapi.dto.RegisterRequest;
import com.byteshifttech.userapi.exception.CustomAppException;
import com.byteshifttech.userapi.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import(AuthControllerTest.AuthControllerTestConfig.class)
class AuthControllerTest {

    @TestConfiguration
    static class AuthControllerTestConfig {
        @Bean
        public AuthService authService() {
            return mock(AuthService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;

    @BeforeEach
    void setup() {
        registerRequest = new RegisterRequest("John Doe", "john@example.com", "securePass");
        loginRequest = new LoginRequest("john@example.com", "securePass");
    }

    @Test
    @Operation(summary = "Register a new user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    void shouldRegisterUserSuccessfully() throws Exception {
        when(authService.register(any())).thenReturn(new AuthResponse("token123", "User registered successfully"));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value("token123"))
                .andExpect(jsonPath("$.message").value("User registered successfully"));
    }

    @Test
    @Operation(summary = "Login existing user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "400", description = "Invalid credentials or input")
    })
    void shouldLoginUserSuccessfully() throws Exception {
        when(authService.authenticate(any())).thenReturn(new AuthResponse("token456", "Login successful"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token456"))
                .andExpect(jsonPath("$.message").value("Login successful"));
    }

    @Test
    void shouldFailRegistrationWithMissingFields() throws Exception {
        RegisterRequest invalidRequest = new RegisterRequest("", "", "");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldFailLoginWithInvalidCredentials() throws Exception {
        when(authService.authenticate(any())).thenThrow(new CustomAppException("Invalid credentials", 400));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid credentials"));
    }

    @Test
    void shouldFailRegistrationWithDuplicateEmail() throws Exception {
        when(authService.register(any())).thenThrow(new CustomAppException("Email is already registered", 409));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Email is already registered"));
    }

    @Test
    void shouldFailLoginWithMissingEmail() throws Exception {
        LoginRequest invalidRequest = new LoginRequest("", "somePass");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}
