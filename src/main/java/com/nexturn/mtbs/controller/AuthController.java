package com.nexturn.mtbs.controller;

import com.nexturn.mtbs.dto.response.UserResponse;
import com.nexturn.mtbs.entity.User;
import com.nexturn.mtbs.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @RequestBody User user) {

        User registeredUser = authService.register(user);

        return ResponseEntity.ok(
                toUserResponse(registeredUser)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(
            @RequestParam String email,
            @RequestParam String password) {

        User loggedInUser =
                authService.login(email, password);

        return ResponseEntity.ok(
                toUserResponse(loggedInUser)
        );
    }

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
}