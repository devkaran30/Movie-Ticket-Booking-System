package com.nexturn.mtbs.dto.request;

import com.nexturn.mtbs.enums.TheatreStatus;

public class TheatreRequest {

    private TheatreStatus status;

    public TheatreRequest() {
    }

    public TheatreStatus getStatus() {
        return status;
    }

    public void setStatus(TheatreStatus status) {
        this.status = status;
    }
}