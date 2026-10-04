package com.nexturn.mtbs.controller;

import com.nexturn.mtbs.dto.request.PaymentRequest;
import com.nexturn.mtbs.dto.response.PaymentResponse;
import com.nexturn.mtbs.entity.Payment;
import com.nexturn.mtbs.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // Convert Payment entity to PaymentResponse
    private PaymentResponse toPaymentResponse(Payment payment) {

        return new PaymentResponse(
                payment.getId(),
                payment.getBooking().getId(),
                payment.getTransactionId(),
                payment.getAmount(),
                payment.getPaymentMethod(),
                payment.getPaymentStatus(),
                payment.getPaymentTime()
        );
    }

    // Basic payment creation
    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(
            @RequestBody Payment payment) {

        Payment savedPayment = paymentService.createPayment(payment);

        return ResponseEntity.ok(
                toPaymentResponse(savedPayment)
        );
    }

    // Real payment processing
    @PostMapping("/process")
    public ResponseEntity<PaymentResponse> processPayment(
            @RequestBody PaymentRequest paymentRequest) {

        Payment payment = paymentService.processPayment(paymentRequest);

        return ResponseEntity.ok(
                toPaymentResponse(payment)
        );
    }

    // Get all payments
    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getAllPayments() {

        List<PaymentResponse> payments = paymentService.getAllPayments()
                .stream()
                .map(this::toPaymentResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(payments);
    }

    // Get payment by ID
    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPaymentById(
            @PathVariable Long id) {

        Payment payment = paymentService.getPaymentById(id);

        return ResponseEntity.ok(
                toPaymentResponse(payment)
        );
    }

    // Update payment
    @PutMapping("/{id}")
    public ResponseEntity<PaymentResponse> updatePayment(
            @PathVariable Long id,
            @RequestBody Payment payment) {

        Payment updatedPayment =
                paymentService.updatePayment(id, payment);

        return ResponseEntity.ok(
                toPaymentResponse(updatedPayment)
        );
    }

    // Delete payment
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(
            @PathVariable Long id) {

        paymentService.deletePayment(id);

        return ResponseEntity.noContent().build();
    }
}