package com.nexturn.mtbs.service;

import com.nexturn.mtbs.entity.User;
import com.nexturn.mtbs.enums.UserStatus;

import java.util.List;

public interface UserService {

    User createUser(User user);

    User getUserById(Long id);

    List<User> getAllUsers();

    User updateUser(Long id, User user);

    User updateUserStatus(Long id, UserStatus status);

    void deleteUser(Long id);
}