package com.nexturn.mtbs.service;

import com.nexturn.mtbs.dto.request.PaymentRequest;
import com.nexturn.mtbs.entity.Payment;

import java.util.List;

public interface PaymentService {

    Payment createPayment(Payment payment);

    Payment processPayment(PaymentRequest paymentRequest);

    Payment getPaymentById(Long id);

    List<Payment> getAllPayments();

    Payment updatePayment(Long id, Payment payment);

    void deletePayment(Long id);
}