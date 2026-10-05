package com.nexturn.mtbs.dto.request;

import com.nexturn.mtbs.enums.SeatStatus;

public class SeatRequest {

    private SeatStatus status;

    public SeatRequest() {
    }

    public SeatStatus getStatus() {
        return status;
    }

    public void setStatus(SeatStatus status) {
        this.status = status;
    }
}