package com.nexturn.mtbs.service.impl;

import com.nexturn.mtbs.entity.Refund;
import com.nexturn.mtbs.repository.RefundRepository;
import com.nexturn.mtbs.service.RefundService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RefundServiceImpl implements RefundService {

    private final RefundRepository refundRepository;

    public RefundServiceImpl(RefundRepository refundRepository) {
        this.refundRepository = refundRepository;
    }

    @Override
    public Refund createRefund(Refund refund) {
        return refundRepository.save(refund);
    }

    @Override
    public Refund getRefundById(Long id) {
        return refundRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Refund not found with id: " + id));
    }

    @Override
    public List<Refund> getAllRefunds() {
        return refundRepository.findAll();
    }

    @Override
    public Refund updateRefund(Long id, Refund refund) {

        Refund existingRefund = getRefundById(id);

        existingRefund.setPayment(refund.getPayment());
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