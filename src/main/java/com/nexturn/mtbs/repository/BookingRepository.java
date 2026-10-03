package com.nexturn.mtbs.repository;

import com.nexturn.mtbs.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}