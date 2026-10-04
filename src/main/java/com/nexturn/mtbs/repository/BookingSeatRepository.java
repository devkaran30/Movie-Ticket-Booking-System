package com.nexturn.mtbs.repository;

import com.nexturn.mtbs.entity.BookingSeat;
import com.nexturn.mtbs.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingSeatRepository
        extends JpaRepository<BookingSeat, Long> {

    List<BookingSeat> findByBookingId(Long bookingId);

    List<BookingSeat> findBySeatId(Long seatId);

    List<BookingSeat> findBySeatIdAndBooking_Show_IdAndBooking_BookingStatus(
            Long seatId,
            Long showId,
            BookingStatus bookingStatus
    );
}