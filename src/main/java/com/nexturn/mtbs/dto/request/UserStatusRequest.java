package com.nexturn.mtbs.dto.request;

import com.nexturn.mtbs.enums.UserStatus;

public class UserStatusRequest {

    private UserStatus status;

    public UserStatusRequest() {
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }
}