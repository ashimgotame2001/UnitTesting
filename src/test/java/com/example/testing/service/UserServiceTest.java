package com.example.testing.service;

import com.example.testing.entity.User;
import com.example.testing.repo.UserRepository;
import com.example.testing.validations.UserCreationValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserCreationValidator validator;

    @InjectMocks
    private UserService userService;

    private User validUser;

    @BeforeEach
    void setUp() {
        validUser = new User(null, "testUser", "test@example.com");
    }

    @Test
    void createUser_shouldValidateBeforeSaving() {
        // Arrange
        User savedUser = new User(1L, "testUser", "test@example.com");
        doNothing().when(validator).validate(validUser);
        when(userRepository.save(validUser)).thenReturn(savedUser);

        // Act
        User result = userService.createUser(validUser);

        // Assert
        assertThat(result.getId()).isEqualTo(1L);
        verify(validator).validate(validUser);
        verify(userRepository).save(validUser);
    }

    @Test
    void createUser_shouldNotSaveWhenValidationFails() {
        // Arrange
        doThrow(new IllegalArgumentException("Validation failed"))
                .when(validator)
                .validate(validUser);

        // Act & Assert
        assertThatThrownBy(() -> userService.createUser(validUser))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Validation failed");

        verify(validator).validate(validUser);
        verify(userRepository, never()).save(any());
    }
}
