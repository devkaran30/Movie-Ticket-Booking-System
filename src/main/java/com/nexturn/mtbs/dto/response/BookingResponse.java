package com.nexturn.mtbs.dto.response;

import com.nexturn.mtbs.enums.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class BookingResponse {

    private Long id;
    private String bookingNumber;
    private Long userId;
    private Long showId;
    private List<Long> seatIds;
    private BigDecimal totalAmount;
    private BookingStatus bookingStatus;
    private LocalDateTime bookingTime;

    public BookingResponse() {
    }

    public BookingResponse(Long id,
                           String bookingNumber,
                           Long userId,
                           Long showId,
                           List<Long> seatIds,
                           BigDecimal totalAmount,
                           BookingStatus bookingStatus,
                           LocalDateTime bookingTime) {
        this.id = id;
        this.bookingNumber = bookingNumber;
        this.userId = userId;
        this.showId = showId;
        this.seatIds = seatIds;
        this.totalAmount = totalAmount;
        this.bookingStatus = bookingStatus;
        this.bookingTime = bookingTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookingNumber() {
        return bookingNumber;
    }

    public void setBookingNumber(String bookingNumber) {
        this.bookingNumber = bookingNumber;
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

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(LocalDateTime bookingTime) {
        this.bookingTime = bookingTime;
    }
}