package com.nexturn.mtbs.controller;

import com.nexturn.mtbs.dto.response.RefundResponse;
import com.nexturn.mtbs.entity.Refund;
import com.nexturn.mtbs.service.RefundService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/refunds")
public class RefundController {

    private final RefundService refundService;

    public RefundController(RefundService refundService) {
        this.refundService = refundService;
    }

    private RefundResponse toRefundResponse(Refund refund) {

        return new RefundResponse(
                refund.getId(),
                refund.getRefundAmount(),
                refund.getRefundReason(),
                refund.getRefundStatus(),
                refund.getRefundTime(),
                refund.getRefundTransactionId(),
                refund.getPayment().getId()
        );
    }

    @PostMapping
    public ResponseEntity<RefundResponse> createRefund(
            @RequestBody Refund refund) {

        Refund savedRefund =
                refundService.createRefund(refund);

        return ResponseEntity.ok(
                toRefundResponse(savedRefund)
        );
    }

    @GetMapping
    public ResponseEntity<List<RefundResponse>> getAllRefunds() {

        List<RefundResponse> refunds =
                refundService.getAllRefunds()
                        .stream()
                        .map(this::toRefundResponse)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(refunds);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RefundResponse> getRefundById(
            @PathVariable Long id) {

        Refund refund =
                refundService.getRefundById(id);

        return ResponseEntity.ok(
                toRefundResponse(refund)
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RefundResponse>> getRefundsByUserId(
            @PathVariable Long userId) {

        List<RefundResponse> refunds =
                refundService.getRefundsByUserId(userId)
                        .stream()
                        .map(this::toRefundResponse)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(refunds);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RefundResponse> updateRefund(
            @PathVariable Long id,
            @RequestBody Refund refund) {

        Refund updatedRefund =
                refundService.updateRefund(id, refund);

        return ResponseEntity.ok(
                toRefundResponse(updatedRefund)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRefund(
            @PathVariable Long id) {

        refundService.deleteRefund(id);

        return ResponseEntity.noContent().build();
    }
}