package com.example.testing.repo;

/*
 * @Created At 02/07/2025
 * @Author ashim.gotame
 */

import com.example.testing.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    // Keep your existing methods
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
