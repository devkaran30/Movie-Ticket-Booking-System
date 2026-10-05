package com.nexturn.mtbs.service;

import com.nexturn.mtbs.dto.request.BookingRequest;
import com.nexturn.mtbs.entity.Booking;

import java.util.List;

public interface BookingService {

    Booking createBooking(Booking booking);

    Booking createRealBooking(BookingRequest bookingRequest);

    Booking cancelBooking(Long bookingId);

    Booking getBookingById(Long id);

    List<Booking> getAllBookings();

    List<Booking> getBookingsByUserId(Long userId);

    Booking updateBooking(Long id, Booking booking);

    void deleteBooking(Long id);
}