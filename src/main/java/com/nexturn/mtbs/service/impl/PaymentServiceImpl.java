package com.nexturn.mtbs.service.impl;

import com.nexturn.mtbs.entity.Payment;
import com.nexturn.mtbs.repository.PaymentRepository;
import com.nexturn.mtbs.service.PaymentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Payment createPayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override
    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Payment not found with id: " + id));
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    @Override
    public Payment updatePayment(Long id, Payment payment) {

        Payment existingPayment = getPaymentById(id);

        existingPayment.setBooking(payment.getBooking());
        existingPayment.setTransactionId(payment.getTransactionId());
        existingPayment.setAmount(payment.getAmount());
        existingPayment.setPaymentMethod(payment.getPaymentMethod());
        existingPayment.setPaymentStatus(payment.getPaymentStatus());
        existingPayment.setPaymentTime(payment.getPaymentTime());

        return paymentRepository.save(existingPayment);
    }

    @Override
    public void deletePayment(Long id) {

        Payment existingPayment = getPaymentById(id);

        paymentRepository.delete(existingPayment);
    }
}