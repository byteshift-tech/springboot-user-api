package com.byteshifttech.userapi.repository;

import com.byteshifttech.userapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for User entity.
 * Provides basic CRUD operations and custom finder methods.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find user by email address.
     *
     * @param email the email to search
     * @return Optional containing the User if found
     */
    Optional<User> findByEmail(String email);

    /**
     * Check if a user exists by email.
     *
     * @param email the email to check
     * @return true if a user with the email exists
     */
    boolean existsByEmail(String email);
}
