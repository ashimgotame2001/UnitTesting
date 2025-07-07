package com.example.testing.service;

/*
 * @Created At 02/07/2025
 * @Author ashim.gotame
 */

import com.example.testing.entity.User;
import com.example.testing.repo.UserRepository;
import com.example.testing.validations.UserCreationValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;


    private final UserCreationValidator validator;



    @Transactional
    public User createUser(User user) {
        validator.validate(user); // Validate before saving
        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User Not found"));
    }

    public User updateUser(Long id, User userDetails) {
        User user = getUserById(id);
        user.setEmail(userDetails.getEmail());
        return userRepository.save(user);
    }
}
