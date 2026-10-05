package com.nexturn.mtbs.service;

import com.nexturn.mtbs.entity.User;

public interface AuthService {

    User register(User user);

    User login(String email, String password);

    void changePassword(
            Long userId,
            String oldPassword,
            String newPassword,
            String confirmPassword
    );
}