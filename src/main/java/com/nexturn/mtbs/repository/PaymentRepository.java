package com.nexturn.mtbs.repository;

import com.nexturn.mtbs.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}