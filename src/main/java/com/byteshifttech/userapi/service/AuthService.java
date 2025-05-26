package com.byteshifttech.userapi.service;

import com.byteshifttech.userapi.dto.AuthResponse;
import com.byteshifttech.userapi.dto.LoginRequest;
import com.byteshifttech.userapi.dto.RegisterRequest;
import com.byteshifttech.userapi.entity.User;
import com.byteshifttech.userapi.exception.CustomAppException;
import com.byteshifttech.userapi.repository.UserRepository;
import com.byteshifttech.userapi.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for handling user authentication and registration logic.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * Register a new user if email does not already exist.
     *
     * @param request RegisterRequest containing name, email, password
     * @return AuthResponse with token and message
     */
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomAppException("Email is already registered", 409);
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(token, "User registered successfully");
    }

    /**
     * Authenticate user and return a JWT token.
     *
     * @param request LoginRequest containing email and password
     * @return AuthResponse with token and message
     */
    public AuthResponse authenticate(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomAppException("Invalid credentials", 400));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomAppException("Invalid credentials", 400);
        }

        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(token, "Login successful");
    }
}
