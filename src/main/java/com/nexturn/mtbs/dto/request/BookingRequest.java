package com.nexturn.mtbs.dto.request;

import java.math.BigDecimal;
import java.util.List;

public class BookingRequest {

    private Long userId;

    private Long showId;

    private List<Long> seatIds;

    private BigDecimal totalAmount;

    public BookingRequest() {
    }

    public BookingRequest(Long userId, Long showId,
                           List<Long> seatIds,
                           BigDecimal totalAmount) {
        this.userId = userId;
        this.showId = showId;
        this.seatIds = seatIds;
        this.totalAmount = totalAmount;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getShowId() {
        return showId;
    }

    public void setShowId(Long showId) {
        this.showId = showId;
    }

    public List<Long> getSeatIds() {
        return seatIds;
    }

    public void setSeatIds(List<Long> seatIds) {
        this.seatIds = seatIds;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}