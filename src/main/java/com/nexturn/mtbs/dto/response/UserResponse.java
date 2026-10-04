package com.nexturn.mtbs.dto.response;

import com.nexturn.mtbs.enums.UserRole;
import com.nexturn.mtbs.enums.UserStatus;

public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private UserRole role;
    private UserStatus status;

    public UserResponse() {
    }

    public UserResponse(Long id,
                        String name,
                        String email,
                        String phoneNumber,
                        UserRole role,
                        UserStatus status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }
}