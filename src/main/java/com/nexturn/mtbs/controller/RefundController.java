package com.nexturn.mtbs.controller;

import com.nexturn.mtbs.entity.Refund;
import com.nexturn.mtbs.service.RefundService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/refunds")
public class RefundController {

    private final RefundService refundService;

    public RefundController(RefundService refundService) {
        this.refundService = refundService;
    }

    @PostMapping
    public ResponseEntity<Refund> createRefund(
            @RequestBody Refund refund) {

        return ResponseEntity.ok(
                refundService.createRefund(refund)
        );
    }

    @GetMapping
    public ResponseEntity<List<Refund>> getAllRefunds() {

        return ResponseEntity.ok(
                refundService.getAllRefunds()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Refund> getRefundById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                refundService.getRefundById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Refund> updateRefund(
            @PathVariable Long id,
            @RequestBody Refund refund) {

        return ResponseEntity.ok(
                refundService.updateRefund(id, refund)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRefund(
            @PathVariable Long id) {

        refundService.deleteRefund(id);

        return ResponseEntity.noContent().build();
    }
}