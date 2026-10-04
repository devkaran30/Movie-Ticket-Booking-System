package com.nexturn.mtbs.repository;

import com.nexturn.mtbs.entity.Refund;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RefundRepository extends JpaRepository<Refund, Long> {

    Optional<Refund> findByPaymentId(Long paymentId);

    List<Refund> findByPaymentBookingUserId(Long userId);
}