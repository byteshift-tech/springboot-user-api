package com.byteshifttech.userapi.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomAppExceptionTest {

    @Test
    void shouldStoreMessageAndStatusCode() {
        CustomAppException ex = new CustomAppException("Conflict occurred", 409);

        assertEquals("Conflict occurred", ex.getMessage());
        assertEquals(409, ex.getStatusCode());
    }

    @Test
    void shouldBeInstanceOfRuntimeException() {
        CustomAppException ex = new CustomAppException("Bad request", 400);
        assertInstanceOf(RuntimeException.class, ex);
    }
}
