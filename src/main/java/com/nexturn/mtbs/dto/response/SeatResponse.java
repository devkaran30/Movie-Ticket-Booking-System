package com.nexturn.mtbs.dto.response;

import com.nexturn.mtbs.enums.SeatType;

public class SeatResponse {

    private Long id;
    private String seatNumber;
    private String rowNumber;
    private SeatType seatType;
    private String status;

    public SeatResponse() {
    }

    public SeatResponse(
            Long id,
            String seatNumber,
            String rowNumber,
            SeatType seatType,
            String status) {

        this.id = id;
        this.seatNumber = seatNumber;
        this.rowNumber = rowNumber;
        this.seatType = seatType;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(String rowNumber) {
        this.rowNumber = rowNumber;
    }

    public SeatType getSeatType() {
        return seatType;
    }

    public void setSeatType(SeatType seatType) {
        this.seatType = seatType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}