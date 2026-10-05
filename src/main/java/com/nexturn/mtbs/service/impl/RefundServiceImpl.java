package com.nexturn.mtbs.service.impl;

import com.nexturn.mtbs.entity.Payment;
import com.nexturn.mtbs.entity.Refund;
import com.nexturn.mtbs.repository.PaymentRepository;
import com.nexturn.mtbs.repository.RefundRepository;
import com.nexturn.mtbs.service.RefundService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RefundServiceImpl implements RefundService {

    private final RefundRepository refundRepository;
    private final PaymentRepository paymentRepository;

    public RefundServiceImpl(
            RefundRepository refundRepository,
            PaymentRepository paymentRepository) {

        this.refundRepository = refundRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Refund createRefund(Refund refund) {
        return refundRepository.save(refund);
    }

    @Override
    public Refund getRefundById(Long id) {
        return refundRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Refund not found with id: " + id
                        ));
    }

    @Override
    public List<Refund> getAllRefunds() {
        return refundRepository.findAll();
    }

    @Override
    public List<Refund> getRefundsByUserId(Long userId) {
        return refundRepository.findByPaymentBookingUserId(userId);
    }

    @Override
    public Refund updateRefund(Long id, Refund refund) {

        Refund existingRefund = getRefundById(id);

        if (refund.getPayment() != null
                && refund.getPayment().getId() != null) {

            Payment payment = paymentRepository.findById(
                    refund.getPayment().getId()
            ).orElseThrow(() ->
                    new RuntimeException(
                            "Payment not found with id: "
                                    + refund.getPayment().getId()
                    ));

            existingRefund.setPayment(payment);
        }

        existingRefund.setRefundTransactionId(
                refund.getRefundTransactionId()
        );

        existingRefund.setRefundAmount(
                refund.getRefundAmount()
        );

        existingRefund.setRefundReason(
                refund.getRefundReason()
        );

        existingRefund.setRefundStatus(
                refund.getRefundStatus()
        );

        existingRefund.setRefundTime(
                refund.getRefundTime()
        );

        return refundRepository.save(existingRefund);
    }

    @Override
    public void deleteRefund(Long id) {

        Refund existingRefund = getRefundById(id);

        refundRepository.delete(existingRefund);
    }
}