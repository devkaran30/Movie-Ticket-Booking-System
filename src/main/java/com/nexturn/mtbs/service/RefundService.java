package com.nexturn.mtbs.service;

import com.nexturn.mtbs.entity.Refund;

import java.util.List;

public interface RefundService {

    Refund createRefund(Refund refund);

    Refund getRefundById(Long id);

    List<Refund> getAllRefunds();

    Refund updateRefund(Long id, Refund refund);

    void deleteRefund(Long id);
}