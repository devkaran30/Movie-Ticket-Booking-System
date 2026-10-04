package com.nexturn.mtbs.dto.response;

import com.nexturn.mtbs.enums.RefundStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RefundResponse {

    private Long id;
    private BigDecimal refundAmount;
    private String refundReason;
    private RefundStatus refundStatus;
    private LocalDateTime refundTime;
    private String refundTransactionId;
    private Long paymentId;

    public RefundResponse() {
    }

    public RefundResponse(
            Long id,
            BigDecimal refundAmount,
            String refundReason,
            RefundStatus refundStatus,
            LocalDateTime refundTime,
            String refundTransactionId,
            Long paymentId) {

        this.id = id;
        this.refundAmount = refundAmount;
        this.refundReason = refundReason;
        this.refundStatus = refundStatus;
        this.refundTime = refundTime;
        this.refundTransactionId = refundTransactionId;
        this.paymentId = paymentId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getRefundReason() {
        return refundReason;
    }

    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }

    public RefundStatus getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(RefundStatus refundStatus) {
        this.refundStatus = refundStatus;
    }

    public LocalDateTime getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(LocalDateTime refundTime) {
        this.refundTime = refundTime;
    }

    public String getRefundTransactionId() {
        return refundTransactionId;
    }

    public void setRefundTransactionId(String refundTransactionId) {
        this.refundTransactionId = refundTransactionId;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }
}