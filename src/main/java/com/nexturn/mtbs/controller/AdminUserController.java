package com.nexturn.mtbs.controller;

import com.nexturn.mtbs.dto.request.UserStatusRequest;
import com.nexturn.mtbs.dto.response.UserResponse;
import com.nexturn.mtbs.entity.User;
import com.nexturn.mtbs.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    // Convert User entity to UserResponse
    private UserResponse toUserResponse(User user) {

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getRole(),
                user.getStatus()
        );
    }

    // Get all users
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {

        List<UserResponse> users = userService.getAllUsers()
                .stream()
                .map(this::toUserResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(users);
    }

    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable Long id) {

        User user = userService.getUserById(id);

        return ResponseEntity.ok(
                toUserResponse(user)
        );
    }

    // Update user status
    @PutMapping("/{id}/status")
    public ResponseEntity<UserResponse> updateUserStatus(
            @PathVariable Long id,
            @RequestBody UserStatusRequest request) {

        User updatedUser =
                userService.updateUserStatus(
                        id,
                        request.getStatus()
                );

        return ResponseEntity.ok(
                toUserResponse(updatedUser)
        );
    }

    // Delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id) {

        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }
}