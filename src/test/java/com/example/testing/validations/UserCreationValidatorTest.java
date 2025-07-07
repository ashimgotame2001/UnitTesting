package com.example.testing.validations;

import com.example.testing.entity.User;
import com.example.testing.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserCreationValidatorTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserCreationValidator validator;

    private User validUser;

    @BeforeEach
    void setUp() {
        validUser = new User(null, "validUser", "valid@example.com");
    }

    @Test
    void validate_shouldPassForValidUser() {
        when(userRepository.existsByUsername("validUser")).thenReturn(false);
        when(userRepository.existsByEmail("valid@example.com")).thenReturn(false);

        assertDoesNotThrow(() -> validator.validate(validUser));
        verify(userRepository).existsByUsername("validUser");
        verify(userRepository).existsByEmail("valid@example.com");
    }

    @Test
    void validate_shouldFailForNullUser() {
        assertThrows(IllegalArgumentException.class,
                () -> validator.validate(null),
                "User cannot be null");
    }

    @Test
    void validateUsername_shouldFailForEmptyUsername() {
        User user = new User(null, "", "test@example.com");
        assertThrows(IllegalArgumentException.class,
                () -> validator.validate(user),
                "Username cannot be empty");
    }

    @Test
    void validateUsername_shouldFailForShortUsername() {
        User user = new User(null, "abc", "test@example.com");
        assertThrows(IllegalArgumentException.class,
                () -> validator.validate(user),
                "Username must be 4-20 characters");
    }

    @Test
    void validateEmail_shouldFailForInvalidFormat() {
        User user = new User(null, "validUser", "invalid-email");
        assertThrows(IllegalArgumentException.class,
                () -> validator.validate(user),
                "Invalid email format");
    }

    @Test
    void checkUniqueness_shouldFailForExistingUsername() {
        when(userRepository.existsByUsername("validUser")).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> validator.validate(validUser),
                "Username already exists");
    }

    @Test
    void checkUniqueness_shouldFailForExistingEmail() {
        when(userRepository.existsByUsername("validUser")).thenReturn(false);
        when(userRepository.existsByEmail("valid@example.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> validator.validate(validUser),
                "Email already exists");
    }
}
