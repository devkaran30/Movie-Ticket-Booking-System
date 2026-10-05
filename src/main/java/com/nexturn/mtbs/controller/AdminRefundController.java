package com.nexturn.mtbs.controller;

import com.nexturn.mtbs.dto.response.RefundResponse;
import com.nexturn.mtbs.entity.Refund;
import com.nexturn.mtbs.service.RefundService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/refunds")
public class AdminRefundController {

    private final RefundService refundService;

    public AdminRefundController(RefundService refundService) {
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

        Refund refund = refundService.getRefundById(id);

        return ResponseEntity.ok(toRefundResponse(refund));
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