package com.nexturn.mtbs.dto.request;

import com.nexturn.mtbs.enums.PaymentMethod;

import java.math.BigDecimal;

public class PaymentRequest {

    private Long bookingId;
    private String transactionId;
    private BigDecimal amount;
    private PaymentMethod paymentMethod;

    public PaymentRequest() {
    }

    public PaymentRequest(Long bookingId,
                          String transactionId,
                          BigDecimal amount,
                          PaymentMethod paymentMethod) {
        this.bookingId = bookingId;
        this.transactionId = transactionId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}