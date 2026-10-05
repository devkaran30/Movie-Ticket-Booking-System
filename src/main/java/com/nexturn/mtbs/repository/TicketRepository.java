package com.nexturn.mtbs.repository;

import com.nexturn.mtbs.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Optional<Ticket> findByBookingId(Long bookingId);

    List<Ticket> findByBookingUserId(Long userId);
}