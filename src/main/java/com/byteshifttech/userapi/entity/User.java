package com.byteshifttech.userapi.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * User entity representing a registered user in the system.
 * Maps to the "users" table in the database.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    /**
     * Primary key: Auto-generated user ID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Full name of the user.
     */
    @Column(nullable = false)
    private String name;

    /**
     * Unique email address for login and communication.
     */
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * Hashed password (never store raw passwords).
     */
    @Column(nullable = false)
    private String password;

    /**
     * Timestamp when the user was created.
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
