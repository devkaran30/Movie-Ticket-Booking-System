package com.nexturn.mtbs.repository;

import com.nexturn.mtbs.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, Long> {
}