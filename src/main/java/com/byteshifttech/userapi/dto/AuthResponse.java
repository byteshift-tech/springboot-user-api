package com.byteshifttech.userapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO sent after successful authentication or registration.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

    /**
     * JWT token to be used for authenticated requests.
     */
    private String token;

    /**
     * Message or status note.
     */
    private String message;
}
