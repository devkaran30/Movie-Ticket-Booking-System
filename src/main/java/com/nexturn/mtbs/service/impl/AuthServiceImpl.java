package com.nexturn.mtbs.service.impl;

import com.nexturn.mtbs.entity.User;
import com.nexturn.mtbs.repository.UserRepository;
import com.nexturn.mtbs.service.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User register(User user) {
        return userRepository.save(user);
    }

    @Override
    public User login(String email, String password) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Invalid email or password"
                        ));

        if (!user.getPassword().equals(password)) {
            throw new RuntimeException(
                    "Invalid email or password"
            );
        }

        return user;
    }

    @Override
    public void changePassword(
            Long userId,
            String oldPassword,
            String newPassword,
            String confirmPassword) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found with id: " + userId
                        ));

        if (!user.getPassword().equals(oldPassword)) {
            throw new RuntimeException(
                    "Old password is incorrect"
            );
        }

        if (newPassword == null || newPassword.isBlank()) {
            throw new RuntimeException(
                    "New password cannot be empty"
            );
        }

        if (!newPassword.equals(confirmPassword)) {
            throw new RuntimeException(
                    "New password and confirm password do not match"
            );
        }

        user.setPassword(newPassword);

        userRepository.save(user);
    }
}