package com.byteshifttech.userapi.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "jwtSecret", "test_secret_key");
        ReflectionTestUtils.setField(jwtUtil, "jwtExpirationInMs", 3600000L); // 1 hour
    }

    @Test
    void shouldGenerateAndValidateToken() {
        String email = "user@example.com";
        String token = jwtUtil.generateToken(email);

        assertNotNull(token);
        assertEquals(email, jwtUtil.extractUsername(token));
        assertTrue(jwtUtil.validateToken(token, email));
    }

    @Test
    void shouldInvalidateTokenWithWrongUsername() {
        String email = "user@example.com";
        String token = jwtUtil.generateToken(email);

        assertFalse(jwtUtil.validateToken(token, "wrong@example.com"));
    }

    @Test
    void shouldThrowExceptionForMalformedToken() {
        String invalidToken = "not.a.valid.jwt";
        assertThrows(Exception.class, () -> jwtUtil.extractUsername(invalidToken));
    }
}