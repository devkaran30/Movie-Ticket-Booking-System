package com.nexturn.mtbs.dto.request;

import com.nexturn.mtbs.enums.ScreenStatus;

public class ScreenRequest {

    private ScreenStatus status;

    public ScreenRequest() {
    }

    public ScreenStatus getStatus() {
        return status;
    }

    public void setStatus(ScreenStatus status) {
        this.status = status;
    }
}